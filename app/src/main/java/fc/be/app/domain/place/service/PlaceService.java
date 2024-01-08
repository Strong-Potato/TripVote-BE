package fc.be.app.domain.place.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fc.be.app.domain.place.dto.*;
import fc.be.app.domain.place.exception.PlaceException;
import fc.be.app.domain.place.repository.PlaceRepository;
import fc.be.openapi.tourapi.TourAPIService;
import fc.be.openapi.tourapi.dto.bone.PlaceDTO;
import fc.be.openapi.tourapi.tools.ObjectMerger;
import fc.be.openapi.tourapi.tools.TourAPIDomainConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static fc.be.app.domain.place.exception.PlaceErrorCode.PLACE_NOT_LOADED;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {
    private final TourAPIService tourAPIService;
    private final TourAPIDomainConverter tourAPIDomainConverter;
    private final PlaceRepository placeRepository;

    public PlaceInfoGetResponse bringPlaceInfo(final int placeId, final int contentTypeId) {
        Class<? extends PlaceDTO> placeChildClass = tourAPIDomainConverter.convertPlaceToChildDomain(contentTypeId);

        PlaceDTO place = ObjectMerger.merge(placeChildClass,
                tourAPIService.bringDetailCommonDomain(placeId, contentTypeId),
                tourAPIService.bringDetailImageDomains(placeId, contentTypeId),
                tourAPIService.bringDetailIntroDomain(placeId, contentTypeId)
        );

        if (place == null) {
            throw new PlaceException(PLACE_NOT_LOADED);
        }

        return new PlaceInfoGetResponse(place);
    }

    @Transactional
    public boolean insertPlaceInfo(int placeId, PlaceInfoInsertRequest placeInfoInsertRequest) {
        if (placeRepository.existsById(placeId)) {
            log.info("이미 존재하는 placeId 입니다. Mysql DB에 저장되진 않습니다");
            return false;
        }

        placeRepository.save(placeInfoInsertRequest.to(placeId));
        return true;
    }

    public PlaceSearchResponse bringSearchKeywordResults(
            int pageNo, int numOfRows,
            int areaCode, int sigunguCode,
            int contentTypeId,
            String keyword,
            char sortedBy
    ) {

        List<PlaceDTO> places = tourAPIService.bringSearchKeywordDomains(
                pageNo, numOfRows,
                areaCode, sigunguCode,
                contentTypeId,
                keyword,
                sortedBy
        );

        if (places == null) {
            throw new PlaceException(PLACE_NOT_LOADED);
        }

        return PlaceSearchResponse.from(places);
    }

    public PlaceNearbyResponse bringNearbyPlaces(
            int numOfRows, int areaCode,
            int sigunguCode, int contentTypeId
    ) {
        int defaultPageNo = 1;

        List<PlaceDTO> places = tourAPIService.bringAreaBasedSyncDomains(
                defaultPageNo, numOfRows,
                areaCode, sigunguCode,
                contentTypeId
        );

        if (places == null) {
            throw new PlaceException(PLACE_NOT_LOADED);
        }

        return PlaceNearbyResponse.from(places);
    }

    public PlacePopularGetResponse bringPopularPlaces(int numOfRows) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        TypeReference<List<PlaceDTO>> typeReference = new TypeReference<>() {
        };

        List<PlaceDTO> places;

        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/popular-places.json")) {
            places = objectMapper.readValue(inputStream, typeReference);
            places.subList(0, Math.min(numOfRows, places.size()));
        } catch (IOException e) {
            log.warn("30일간 인기 여행지를 가져오지 못했습니다: {}", e.getMessage());
            places = Collections.emptyList();
        }

        return PlacePopularGetResponse.from(places);
    }

}
