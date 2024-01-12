package fc.be.openapi.google.dto.review.form;

import fc.be.openapi.google.dto.review.GoogleReviewResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record GoogleConverter(List<Item> reviews) {

    public static GoogleConverter from(GoogleReviewResponse googleReviewResponse) {
        List<Item> items = new ArrayList<>();
        for(var review : googleReviewResponse.reviews()){
            items.add(
                    new Item(
                            review.authorAttribution().displayName(),
                            review.authorAttribution().photoUri(),
                            review.rating(),
                            LocalDate.parse(review.publishTime()),
                            review.originalText().text(),
                            Boolean.TRUE
                    )
            );
        }
        return new GoogleConverter(items);
    }

    public record Item(
            String nickname,
            String profile,
            Integer rating,
            LocalDate visitedAt,
            String content,
            Boolean isGoogle
    ){

    }
}