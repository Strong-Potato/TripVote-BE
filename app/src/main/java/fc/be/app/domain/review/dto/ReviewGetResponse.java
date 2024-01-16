package fc.be.app.domain.review.dto;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.review.entity.Review;
import fc.be.openapi.google.dto.review.APIReviewResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record ReviewGetResponse(List<Item> reviews) {

    public static ReviewGetResponse from(List<Review> reviews) {
        List<Item> items = new ArrayList<>();
        for (var review : reviews) {
            items.add(new Item(
                    review.getMember().getNickname(),
                    review.getMember().getProfile(),
                    review.getRating(),
                    review.getVisitedAt(),
                    review.getContent(),
                    review.getIsGoogle()
                    )
            );
        }
        return new ReviewGetResponse(items);
    }

    public static List<Review> convertToReviews(APIReviewResponse reviewResponse) {
        List<Review> reviews = new ArrayList<>();
        for (var review : reviewResponse.reviews()) {
            reviews.add(
                    Review.builder()
                            .content(review.content())
                            .rating(review.rating())
                            .member(Member.builder()
                                    .nickname(review.nickname())
                                    .profile(review.profileImage())
                                    .build())
                            .visitedAt(changeLocalDate(review.visitedAt()))
                            .isGoogle(Boolean.TRUE)
                            .build()
            );
        }
        reviews.sort(Comparator.comparing(Review::getVisitedAt).reversed());
        return reviews;
    }

    private static LocalDate changeLocalDate(String strLocalDateTime) {
        DateTimeFormatter formatter = null;
        if (strLocalDateTime.contains("T")) {
            formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
            LocalDateTime localDateTime = LocalDateTime.parse(strLocalDateTime, formatter);
            return localDateTime.toLocalDate();
        }
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(strLocalDateTime, formatter);
    }

    public record Item(
            String nickname,
            String profileImage,
            Integer rating,
            LocalDate visitedAt,
            String content,
            Boolean isGoogle
    ) {
    }
}
