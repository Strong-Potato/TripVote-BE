package fc.be.openapi.google.service;

import fc.be.openapi.google.dto.review.GoogleReviewResponse;
import fc.be.openapi.google.dto.review.form.GoogleRatingResponse;
import fc.be.openapi.google.dto.search.GoogleSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "googlePlacesClient", url = "${google.places.api.base-url}")
public interface GooglePlacesClient {

    @PostMapping("/v1/places:searchText")
    GoogleSearchResponse searchText(
            @RequestHeader("X-Goog-Api-Key") String apiKey,
            @RequestHeader("X-Goog-FieldMask") String fieldMask,
            @RequestBody Map<String, String> textQuery
    );

    @GetMapping("/v1/places/{placeId}")
    GoogleReviewResponse searchReview(
            @RequestHeader("X-Goog-Api-Key") String apiKey,
            @RequestHeader("X-Goog-FieldMask") String fieldMask,
            @RequestHeader("Accept-Language") String language,
            @PathVariable String placeId
    );

    @GetMapping("/v1/places/{placeId}")
    GoogleRatingResponse searchRating(
            @RequestHeader("X-Goog-Api-Key") String apiKey,
            @RequestHeader("X-Goog-FieldMask") String fieldMask,
            @RequestHeader("Accept-Language") String language,
            @PathVariable String placeId
    );
}