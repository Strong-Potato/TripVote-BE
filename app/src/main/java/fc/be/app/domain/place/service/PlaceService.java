package fc.be.app.domain.place.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fc.be.app.domain.place.Place;
import fc.be.app.domain.place.dto.*;
import fc.be.app.domain.place.exception.PlaceException;
import fc.be.app.domain.place.repository.PlaceRepository;
import fc.be.openapi.algolia.SearchEngineService;
import fc.be.openapi.google.service.ReviewAPIService;
import fc.be.openapi.tourapi.TourAPIService;
import fc.be.openapi.tourapi.dto.response.bone.PlaceDTO;
import fc.be.openapi.tourapi.tools.category.CategoryFinder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static fc.be.app.domain.place.exception.PlaceErrorCode.PLACE_NOT_LOADED;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {
    private final TourAPIService tourAPIService;
    private final PlaceRepository placeRepository;
    private final ReviewAPIService reviewAPIService;
    private final SearchEngineService searchEngineService;

    private List<PlaceDTO> popularPlaces = Collections.emptyList();

    @Transactional
    public Place saveOrUpdatePlace(final int placeId, final int placeTypeId) {
        PlaceDTO placeDTO = tourAPIService.bringDetailDomain(
                new PlaceInfoGetRequest(placeId, placeTypeId).toTourAPIRequest()
        );

        Place newPlace = new PlaceInfoGetResponse(placeDTO).toPlace();
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

    public PlaceInfoGetResponse bringPlaceInfo(PlaceInfoGetRequest placeInfoGetRequest) {
        PlaceDTO place = tourAPIService.bringDetailDomain(placeInfoGetRequest.toTourAPIRequest());

        if (place == null) {
            throw new PlaceException(PLACE_NOT_LOADED);
        }

        String categoryCode = place.getCategory();
        String categoryName = CategoryFinder.getCategoryByCode(place.getCategory());
        searchEngineService.saveSearchHistory(categoryCode, categoryName);

        return new PlaceInfoGetResponse(place);
    }

    public PlaceSearchResponse bringSearchKeywordResults(PlaceSearchRequest placeSearchRequest) {
        List<PlaceDTO> places = tourAPIService.bringSearchKeywordDomains(placeSearchRequest.toTourAPIRequest());

        if (places == null) {
            throw new PlaceException(PLACE_NOT_LOADED);
        }

        return PlaceSearchResponse.from(places);
    }

    public PlaceNearbyResponse bringNearbyPlaces(PlaceNearbyRequest placeNearbyRequest) {
        List<PlaceDTO> places = tourAPIService.bringAreaBasedSyncDomains(placeNearbyRequest.toTourAPIRequest());

        if (places == null) {
            throw new PlaceException(PLACE_NOT_LOADED);
        }

        return PlaceNearbyResponse.from(places);
    }

    public PlacePopularGetResponse bringPopularPlaces(PlacePopularGetRequest placePopularGetRequest) {
        final Integer placeTypeId = placePopularGetRequest.placeTypeId();

        List<PlaceDTO> places = popularPlaces.stream()
                .filter(place -> checkPlaceTypeIdNullOrSame(placeTypeId, place.getContentTypeId()))
                .limit(placePopularGetRequest.size())
                .toList();

        return PlacePopularGetResponse.from(places);
    }

    private boolean checkPlaceTypeIdNullOrSame(Integer placeTypeId, Integer contentTypeId) {
        return placeTypeId == null || Objects.equals(contentTypeId, placeTypeId);
    }

    @PostConstruct
    private void loadPopularPlaces() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/popular-places.json")) {
            popularPlaces = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            log.warn("30일간 인기 여행지를 가져오지 못했습니다: {}", e.getMessage());
        }
    }

    public PlacePopularKeywordsResponse bringPopularCategories(Integer size) {
        return PlacePopularKeywordsResponse.from(searchEngineService.bringPopularCategories(size));
    }
}
