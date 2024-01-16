package fc.be.openapi.tourapi.tools;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fc.be.openapi.tourapi.exception.TourAPIError;
import fc.be.openapi.tourapi.exception.TourAPIXMLErrorResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.util.Optional;

import static fc.be.openapi.tourapi.exception.TourAPIErrorCode.COMMUNICATOR_WRONG_REQUEST;
import static fc.be.openapi.tourapi.exception.TourAPIErrorCode.WRONG_XML_FORMAT;

/**
 * Tour API 통신을 위한 유틸리티 클래스
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class Communicator {
    private static final RestTemplate restTemplate = new RestTemplate();

    static {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        restTemplate.setUriTemplateHandler(factory);

        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToEnable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .build();

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);

        restTemplate.getMessageConverters().addFirst(converter);
    }

    /**
     * TourAPI와의 통신 결과 결과를 반환한다.
     * <br> <b>성공</b> -> 해당 DTO(TourAPI 기능명으로 작성) 반환
     * <br> <b>실패</b> -> 예외 결과(XML) 반환
     *
     * @param url          TourAPI 통신 URL
     * @param responseType 반환할 DTO 타입
     * @return TourAPI 통신 결과
     */
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 500))
    public <T> Optional<T> fetchDataFromAPI(final StringBuilder url, final Class<T> responseType) {
        HttpEntity<String> httpEntity = createHttpEntity();

        log.info("Tour API 요청 URL : {}", url);

        return restTemplate.execute(
                url.toString(),
                HttpMethod.GET,
                requestCallback -> {
                },
                clientHttpResponse -> {
                    if (isErrorResponse(clientHttpResponse)) {
                        handleErrorResponse(clientHttpResponse);
                        return Optional.empty();
                    }

                    T result = new HttpMessageConverterExtractor<>(responseType, restTemplate.getMessageConverters())
                            .extractData(clientHttpResponse);

                    return Optional.ofNullable(result);
                },
                httpEntity
        );
    }

    private HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    private boolean isErrorResponse(ClientHttpResponse clientHttpResponse) {
        MediaType contentType = clientHttpResponse.getHeaders().getContentType();
        return contentType != null && contentType.includes(MediaType.TEXT_XML);
    }

    private void handleErrorResponse(ClientHttpResponse clientHttpResponse) {
        try (clientHttpResponse) {
            TourAPIKeyChanger.changeNextKey();

            JAXBContext jaxbContext = JAXBContext.newInstance(TourAPIXMLErrorResponse.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            var tourAPIXMLErrorResponse = (TourAPIXMLErrorResponse) unmarshaller.unmarshal(clientHttpResponse.getBody());

            String errorMessage = tourAPIXMLErrorResponse.getErrorHeader().getReturnAuthMsg();
            new TourAPIError(COMMUNICATOR_WRONG_REQUEST, errorMessage);
            log.warn("원치않은 결과로 TourAPI 에 재요청합니다");
            throw new UnsupportedOperationException();
        } catch (JAXBException | IOException e) {
            new TourAPIError(WRONG_XML_FORMAT, e.getCause());
        }
    }
}