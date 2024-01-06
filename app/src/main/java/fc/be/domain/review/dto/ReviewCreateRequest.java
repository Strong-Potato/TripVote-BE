package fc.be.domain.review.dto;

import fc.be.domain.place.Place;
import fc.be.domain.review.entity.Review;
import fc.be.tourapi.constant.ContentTypeId;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;

public record ReviewCreateRequest(
        @NotNull Long placeId,
        @NotNull String thumbnail, //장소 썸네일 사진 1장
        @NotNull ContentTypeId contentTypeId, //레코드에 이넘 ㄱㅊ?
        @NotNull String title,
        @NotNull Integer areaCode,
        @Min(1) Integer rating,
        @NotBlank String content,
        List<String> images,
        @PastOrPresent LocalDate visitedAt
) {

    public Review toEntity(ReviewCreateRequest reviewCreateRequest){
        return Review.builder()
                .member()
                .place()
                .visitedAt()
                .rating()
                .images()
                .content()
                .build();
    }
}
