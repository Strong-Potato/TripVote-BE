package fc.be.openapi.google.service;

import fc.be.openapi.google.ReviewJsonReader;
import fc.be.openapi.google.dto.review.APIRatingResponse;
import fc.be.openapi.google.dto.review.APIReviewResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class JsonReviewService implements ReviewAPIService {

    @Override
    public APIReviewResponse bringReview(String textQuery, Integer contentTypeId) {
        return ReviewJsonReader.readReviewsJsonFile
                ("./review-example/reviews/place_" + contentTypeId + ".json");
    }

    @Override
    public APIRatingResponse bringRatingCount(String textQuery, Integer contentTypeId) {
        return ReviewJsonReader.readReviewRatingJsonFile(
                "./review-example/rating/place_rating_" + contentTypeId + ".json"
        );
    }
}
