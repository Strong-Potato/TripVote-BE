package fc.be.app.domain.review.dto;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.review.entity.Review;
import fc.be.openapi.google.dto.review.GoogleReviewResponse;
import fc.be.openapi.google.dto.review.GoogleTempReviewResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public record ReviewGetResponse(
        List<Item> reviews
) {

    public static ReviewGetResponse from(
            List<Review> reviews
    ) {
        List<Item> items = new ArrayList<>();
        for (var review : reviews) {
            items.add(
                    new Item(
                            review.getMember().getNickname(),
                            review.getMember().getEmail(),// todo [Review] Member Filed 추가되면 작업 필요
                            review.getRating(),
                            review.getVisitedAt(),
                            review.getContent(),
                            review.getIsGoogle()
                    )
            );
        }
        return new ReviewGetResponse(
                items
        );
    }

    //todo 지워야함
    public static ReviewGetResponse Testfrom(
            GoogleTempReviewResponse googleTempReviewResponse
    ) {
        List<Item> items = new ArrayList<>();
        for (var review : googleTempReviewResponse.reviews()) {
            items.add(
                    new Item(
                            review.nickname(),
                            review.profileImage(),// todo [Review] Member Filed 추가되면 작업 필요
                            review.rating(),
                            TestchangeLocalDate(review.visitedAt()),
                            review.content(),
                            review.isGoogle()
                    )
            );
        }
        return new ReviewGetResponse(
                items
        );
    }

    public static List<Review> convertToReviews(GoogleReviewResponse googleReviewResponse) {
        List<Review> reviews = new ArrayList<>();
        for (var review : googleReviewResponse.reviews()) {
            reviews.add(
                    Review.builder()
                            .content(review.originalText().text())
                            .rating(review.rating())
                            .member(Member.builder()
                                    .nickname(review.authorAttribution().displayName())
                                    .email(review.authorAttribution().photoUri())//todo [Review] 프로필사진 업데이트 요망
                                    .build())
                            .visitedAt(changeLocalDate(review.publishTime()))
                            .isGoogle(Boolean.TRUE)
                            .build()
            );
        }
        reviews.sort((r1, r2) -> r2.getVisitedAt().compareTo(r1.getVisitedAt()));
        return reviews;
    }

    //todo 지워야함
    public static List<Review> TestconvertToReviews(GoogleTempReviewResponse googleReviewResponse) {
        List<Review> reviews = new ArrayList<>();
        for (var review : googleReviewResponse.reviews()) {
            reviews.add(
                    Review.builder()
                            .content(review.content())
                            .rating(review.rating())
                            .member(Member.builder()
                                    .nickname(review.nickname())
                                    .email(review.profileImage())//todo [Review] 프로필사진 업데이트 요망
                                    .build())
                            .visitedAt(TestchangeLocalDate(review.visitedAt()))
                            .isGoogle(Boolean.TRUE)
                            .build()
            );
        }
        reviews.sort((r1, r2) -> r2.getVisitedAt().compareTo(r1.getVisitedAt()));
        return reviews;
    }

    private static LocalDate changeLocalDate(String strLocalDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(strLocalDateTime, formatter);
        return localDateTime.toLocalDate();
    }

    //todo 지워야함
    private static LocalDate TestchangeLocalDate(String strLocalDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
