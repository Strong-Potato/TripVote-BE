package fc.be.openapi.google.service;

import fc.be.openapi.google.dto.review.APIRatingResponse;
import fc.be.openapi.google.dto.review.APIReviewResponse;

public interface ReviewAPIService {

    APIReviewResponse bringReview(String textQuery, Integer contentTypeId);

    APIRatingResponse bringRatingCount(String textQuery, Integer contentTypeId);
}

