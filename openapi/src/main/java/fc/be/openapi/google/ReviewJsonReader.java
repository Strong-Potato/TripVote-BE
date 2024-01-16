package fc.be.openapi.google;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fc.be.openapi.google.dto.review.APIRatingResponse;
import fc.be.openapi.google.dto.review.APIReviewResponse;
import fc.be.openapi.tourapi.exception.TourAPIError;
import fc.be.openapi.tourapi.exception.TourAPIErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ReviewJsonReader {

    public static APIReviewResponse readReviewsJsonFile(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        APIReviewResponse googleReviewResponse = null;
        try (InputStream inputStream = ReviewJsonReader.class.getClassLoader().getResourceAsStream(fileName)) {

            if (inputStream != null) {
                googleReviewResponse = objectMapper.readValue(inputStream, APIReviewResponse.class);
            }
        } catch (IOException e) {
            new TourAPIError(TourAPIErrorCode.NO_CONTENTTPYEID);
        }
        return googleReviewResponse;
    }

    public static APIRatingResponse readReviewRatingJsonFile(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        APIRatingResponse APIRatingResponse = null;
        try (InputStream inputStream = ReviewJsonReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                APIRatingResponse = objectMapper.readValue(inputStream, APIRatingResponse.class);
            }
        } catch (IOException e) {
            new TourAPIError(TourAPIErrorCode.NO_CONTENTTPYEID);
        }
        return APIRatingResponse;
    }

}