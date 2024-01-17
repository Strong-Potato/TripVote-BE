package fc.be.openapi.google.service;

import fc.be.openapi.google.dto.review.APIRatingResponse;
import fc.be.openapi.google.dto.review.APIReviewResponse;
import fc.be.openapi.google.dto.review.GoogleReviewResponse;
import fc.be.openapi.google.dto.review.form.GoogleRatingResponse;
import fc.be.openapi.google.dto.search.GoogleSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Profile("prod")
@RequiredArgsConstructor
public class GoogleReviewService implements ReviewAPIService {

    private final GooglePlacesClient googlePlacesClient;

    @Value("${google.places.api.key}")
    private String apiKey;

    @Override
    public APIReviewResponse bringReview(String textQuery, Integer contentTypeId) {
        var places = bringPlaceId(textQuery).places();
        var googleReviewResponse = places.isEmpty() ?
                new GoogleReviewResponse(null) : searchReview(places.getFirst().id());

        return APIReviewResponse.convertToAPIReviewResponse(googleReviewResponse);
    }

    @Override
    public APIRatingResponse bringRatingCount(String textQuery, Integer contentTypeId) {
        String placeId = bringPlaceId(textQuery).places().getFirst().id();
        var response = searchRating(placeId);
        return APIRatingResponse.convertToRatingResponse(response);
    }

    private GoogleSearchResponse bringPlaceId(String textQuery) {
        return googlePlacesClient.searchText(
                apiKey,
                "places.id,places.name",
                Collections.singletonMap("textQuery", textQuery)
        );
    }

    private GoogleReviewResponse searchReview(String placeId) {
        return googlePlacesClient.searchReview(
                apiKey,
                "reviews",
                "ko",
                placeId
        );
    }

    private GoogleRatingResponse searchRating(String placeId) {
        return googlePlacesClient.searchRating(
                apiKey,
                "rating,userRatingCount",
                "ko",
                placeId
        );
    }
}