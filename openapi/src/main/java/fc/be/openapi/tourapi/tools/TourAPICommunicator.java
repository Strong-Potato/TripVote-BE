package fc.be.openapi.tourapi.tools;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fc.be.openapi.config.TourAPIProperties;
import fc.be.openapi.tourapi.dto.form.diff_property.detail_intro1.DetailIntro1Response;
import fc.be.openapi.tourapi.dto.form.same_property.area_based_sync_list1.AreaBasedSyncList1Response;
import fc.be.openapi.tourapi.dto.form.same_property.detail_common1.DetailCommon1Response;
import fc.be.openapi.tourapi.dto.form.same_property.detail_image1.DetailImage1Response;
import fc.be.openapi.tourapi.dto.form.same_property.search_keyword1.SearchKeyword1Response;
import fc.be.openapi.tourapi.exception.TourAPIXMLErrorResponse;
import fc.be.openapi.tourapi.exception.WrongRequestException;
import fc.be.openapi.tourapi.exception.WrongXMLFormatException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static fc.be.openapi.tourapi.tools.TourAPIKeyChanger.changeNextKey;
import static fc.be.openapi.tourapi.tools.TourAPIKeyChanger.getEncodedKey;

/**
 * Tour API 통신을 위한 유틸리티 클래스
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TourAPICommunicator {
    private static final RestTemplate restTemplate = new RestTemplate();
    private final TourAPIProperties properties;

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
     * Tour API 통신을 위한 URL(필수 쿼리 값 내장)을 생성한다.
     *
     * @param endpoint      API 종단점
     * @param contentTypeId 컨텐츠 타입 ID
     * @param contentId     컨텐츠 ID
     * @return 생성된 URL
     */
    private StringBuilder buildEssentialUrl(
            final String endpoint,
            final int contentTypeId,
            final int contentId
    ) {
        StringBuilder url = new StringBuilder(properties.getBaseUrl() + endpoint);
        url.append("?MobileOS=").append(properties.getMobileOs());
        url.append("&MobileApp=").append(properties.getMobileApp());
        url.append("&_type=").append(properties.getRenderType());

        if (contentTypeId != 0) {
            url.append("&contentTypeId=").append(contentTypeId);
        }

        if (contentId != 0) {
            url.append("&contentId=").append(contentId);
        }

        url.append("&serviceKey=").append(getEncodedKey());

        return url;
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
    private <T> T fetchDataFromAPI(
            final String url,
            final Class<T> responseType
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        log.warn("Tour API 요청 URL : {}", url);

        return restTemplate.execute(
                url,
                HttpMethod.GET,
                requestCallback -> {
                },
                clientHttpResponse -> {
                    MediaType contentType = clientHttpResponse.getHeaders().getContentType();
                    if (contentType != null && contentType.includes(MediaType.TEXT_XML)) {
                        try (clientHttpResponse) {
                            changeNextKey();

                            JAXBContext jaxbContext = JAXBContext.newInstance(TourAPIXMLErrorResponse.class);
                            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                            TourAPIXMLErrorResponse tourAPIXMLErrorResponse = (TourAPIXMLErrorResponse) unmarshaller.unmarshal(clientHttpResponse.getBody());
                            log.error("공공포털 오류 XML 반환 : {}", tourAPIXMLErrorResponse);

                            String errorMessage = tourAPIXMLErrorResponse.getErrorHeader().getReturnAuthMsg();
                            throw new WrongRequestException(errorMessage);
                        } catch (JAXBException e) {
                            throw new WrongXMLFormatException(e);
                        }
                    }

                    return new HttpMessageConverterExtractor<>(responseType, restTemplate.getMessageConverters())
                            .extractData(clientHttpResponse);
                },
                httpEntity
        );
    }

    public SearchKeyword1Response callSearchKeyword(
            final int pageNo,
            final int numOfRows,
            final int areaCode,
            final int sigunguCode,
            final String keyword,
            final int contentTypeId
    ) {
        StringBuilder url = buildEssentialUrl(properties.getSearchKeyword(), contentTypeId, 0);
        url.append("&keyword=").append(keyword == null ? "_" : URLEncoder.encode(keyword, StandardCharsets.UTF_8));

        if (pageNo != 0 && numOfRows != 0) {
            url.append("&pageNo=").append(pageNo);
            url.append("&numOfRows=").append(numOfRows);
        }

        if (areaCode != 0) {
            url.append("&areaCode=").append(areaCode);
        }

        if (areaCode != 0 && sigunguCode != 0) {
            url.append("&sigunguCode=").append(sigunguCode);
        }

        url.append("&arrange=").append("Q");

        return fetchDataFromAPI(
                url.toString(),
                SearchKeyword1Response.class
        );
    }

    public DetailCommon1Response callDetailCommon(
            final int contentId,
            final int contentTypeId
    ) {
        StringBuilder url = buildEssentialUrl(properties.getDetailCommon(), contentTypeId, contentId);
        url.append("&defaultYN=").append("Y");
        url.append("&firstImageYN=").append("Y");
        url.append("&areacodeYN=").append("Y");
        url.append("&catcodeYN=").append("Y");
        url.append("&addrinfoYN=").append("Y");
        url.append("&mapinfoYN=").append("Y");
        url.append("&overviewYN=").append("Y");

        return fetchDataFromAPI(
                url.toString(),
                DetailCommon1Response.class
        );
    }

    public DetailIntro1Response callDetailIntro(
            final int contentId,
            final int contentTypeId
    ) {
        StringBuilder url = buildEssentialUrl(properties.getDetailIntro(), contentTypeId, contentId);

        return fetchDataFromAPI(
                url.toString(),
                DetailIntro1Response.class
        );
    }

    public DetailImage1Response callDetailImage(
            final int contentId
    ) {
        StringBuilder url = buildEssentialUrl(properties.getDetailImage(), 0, contentId);
        url.append("&imageYN=").append("Y");
        url.append("&subImageYN=").append("Y");

        return fetchDataFromAPI(
                url.toString(),
                DetailImage1Response.class
        );
    }

    public AreaBasedSyncList1Response callAreaBasedSync(
            final int pageNo,
            final int numOfRows,
            final int areaCode,
            final int sigunguCode,
            final int contentTypeId
    ) {
        StringBuilder url = buildEssentialUrl(properties.getAreaBasedSyncList(), contentTypeId, 0);
        url.append("&pageNo=").append(pageNo);
        url.append("&numOfRows=").append(numOfRows);
        url.append("&showflag=").append("1");
        url.append("&arrange=").append("Q");

        if (areaCode != 0) {
            url.append("&areaCode=").append(areaCode);
        }
        if (sigunguCode != 0) {
            url.append("&sigunguCode=").append(sigunguCode);
        }

        return fetchDataFromAPI(
                url.toString(),
                AreaBasedSyncList1Response.class
        );
    }
}