package fc.be.app.domain.place.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fc.be.app.domain.place.Place;
import fc.be.app.domain.place.dto.PlaceInfoGetResponse;
import fc.be.app.domain.place.dto.PlaceNearbyResponse;
import fc.be.app.domain.place.dto.PlacePopularGetResponse;
import fc.be.app.domain.place.dto.PlaceSearchResponse;
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

    @Transactional
    public Place saveOrUpdatePlace(final int placeId, final int contentTypeId) {
        Place newPlace = bringPlaceInfo(placeId, contentTypeId).toPlace();
        Place existingPlace = placeRepository.findById(placeId).orElse(null);

        if (existingPlace == null) {
            log.info("새로운 여행지를 저장했습니다: {}", newPlace);
            return placeRepository.save(newPlace);
        } else if (newPlace.getModifiedTime().isAfter(existingPlace.getModifiedTime())) {
            log.info("기존 여행지를 업데이트했습니다: {}", existingPlace);
            existingPlace.update(newPlace);
        } else {
            log.info("기존 여행지가 최신입니다: {}", existingPlace);
        }

        return existingPlace;
    }

    public PlaceInfoGetResponse bringPlaceInfo(final int placeId, final int contentTypeId) {
        PlaceDTO place = getPlaceDTO(placeId, contentTypeId);
        return new PlaceInfoGetResponse(place);
    }

    public PlaceDTO getPlaceDTO(int placeId, int contentTypeId) {
        Class<? extends PlaceDTO> placeChildClass = tourAPIDomainConverter.convertPlaceToChildDomain(contentTypeId);

        PlaceDTO place = ObjectMerger.merge(placeChildClass,
                tourAPIService.bringDetailCommonDomain(placeId, contentTypeId),
                tourAPIService.bringDetailImageDomains(placeId, contentTypeId),
                tourAPIService.bringDetailIntroDomain(placeId, contentTypeId)
        );

        if (place == null) {
            throw new PlaceException(PLACE_NOT_LOADED);
        }
        return place;
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
