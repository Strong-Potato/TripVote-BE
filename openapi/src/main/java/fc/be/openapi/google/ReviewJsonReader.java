package fc.be.openapi.google;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fc.be.openapi.google.dto.review.GoogleTempRatingResponse;
import fc.be.openapi.google.dto.review.GoogleTempReviewResponse;
import fc.be.openapi.tourapi.exception.TourAPIError;
import fc.be.openapi.tourapi.exception.TourAPIErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ReviewJsonReader {

    public static GoogleTempReviewResponse readReviewsJsonFile(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 이 부분을 추가
        GoogleTempReviewResponse googleReviewResponse = null;
        try (InputStream inputStream = ReviewJsonReader.class.getClassLoader().getResourceAsStream(fileName)) {

            if (inputStream != null) {
                googleReviewResponse = objectMapper.readValue(inputStream, GoogleTempReviewResponse.class);
            }
        } catch (IOException e) {
            new TourAPIError(TourAPIErrorCode.NO_CONTENTTPYEID);
        }
        return googleReviewResponse;
    }

    public static GoogleTempRatingResponse googleTempRatingResponse(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        GoogleTempRatingResponse googleTempRatingResponse = null;
        try (InputStream inputStream = ReviewJsonReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                googleTempRatingResponse = objectMapper.readValue(inputStream, GoogleTempRatingResponse.class);
            }
        } catch (IOException e) {
            new TourAPIError(TourAPIErrorCode.NO_CONTENTTPYEID);
        }
        return googleTempRatingResponse;
    }

}