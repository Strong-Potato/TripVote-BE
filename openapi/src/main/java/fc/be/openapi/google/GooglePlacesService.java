package fc.be.openapi.google;

import fc.be.openapi.google.dto.review.GoogleReviewResponse;
import fc.be.openapi.google.dto.review.form.GoogleRatingResponse;
import fc.be.openapi.google.dto.search.GoogleSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GooglePlacesService {

    private final GooglePlacesClient googlePlacesClient;

    @Value("${google.places.api.key}")
    private String apiKey;

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

    public GoogleReviewResponse bringGoogleReview(String textQuery) {
        String placeId = bringPlaceId(textQuery).places().getFirst().id();
        return searchReview(placeId);
    }

    public GoogleRatingResponse bringGoogleRatingCount(String textQuery) {
        String placeId = bringPlaceId(textQuery).places().getFirst().id();
        return searchRating(placeId);
    }
}