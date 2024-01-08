package fc.be.app.domain.review.dto;


import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.place.Location;
import fc.be.app.domain.place.Place;
import fc.be.app.domain.review.entity.Review;
import fc.be.openapi.tourapi.dto.bone.LocationDTO;
import fc.be.openapi.tourapi.dto.bone.PlaceDTO;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

import static java.lang.Boolean.*;

public record ReviewCreateRequest(
        PlaceDTO placeDTO,
        @Positive Integer placeId,
        @NotNull Integer contentTypeId,
        @NotNull String title,
        @NotNull Integer areaCode,
        @Min(1) Integer rating,
        @NotBlank String content,
        List<String> images,
        @PastOrPresent LocalDate visitedAt
) {

    // todo [Review] Member 관련 Security 확인 되는 대로 dto 수정 하겠음 - 민균
    public Review to(Member member) {
        return Review.builder()
                .member(member)
                .place(convertPlace(placeDTO))
                .visitedAt(visitedAt)
                .rating(rating)
                .content(content)
                .images(images)
                .isGoogle(FALSE) //앱 내 리뷰이므로 기본 값 FALSE
                .build();
    }

    private Place convertPlace(PlaceDTO placeDTO) {
        return Place.builder()
                .id(placeDTO.getId())
                .contentTypeId(null)// todo placeDTO.getContentTypeId() 재욱님께 물어보기 Integer or Enum
                .title(placeDTO.getTitle())
                .location(convertLocation(placeDTO.getLocationDTO()))
                .thumbnail(placeDTO.getThumbnail())
                .originalImage(placeDTO.getOriginalImage())
                .createdTime(placeDTO.getCreatedTime())
                .modifiedTime(placeDTO.getModifiedTime())
                .gallery(placeDTO.getGallery())
                .build();
    }

    private Location convertLocation(LocationDTO locationDTO) {
        return Location.builder()
                .address(locationDTO.getAddress())
                .addressDetail(locationDTO.getAddressDetail())
                .phone(locationDTO.getPhone())
                .areaCode(locationDTO.getAreaCode())
                .sigunguCode(locationDTO.getSigunguCode())
                .zipCode(locationDTO.getZipCode())
                .latitude(locationDTO.getLatitude())
                .longitude(locationDTO.getLongitude())
                .build();
    }
}
