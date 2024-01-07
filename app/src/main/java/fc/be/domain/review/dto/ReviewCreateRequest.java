package fc.be.domain.review.dto;

import fc.be.domain.member.entity.Member;
import fc.be.domain.place.Location;
import fc.be.domain.place.Place;
import fc.be.domain.review.entity.Review;
import fc.be.tourapi.constant.ContentTypeId;
import fc.be.tourapi.dto.bone.LocationDTO;
import fc.be.tourapi.dto.bone.PlaceDTO;
import jakarta.validation.constraints.*;

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

    // todo [Review] Member 관련 Security 확인 되는 대로 dto 수정 하겠음 - 민균
    public Review to(ReviewCreateRequest reviewCreateRequest, Member member) {
        return Review.builder()
                .member(member)
                .place(convertPlace(reviewCreateRequest.placeDTO))
                .visitedAt(reviewCreateRequest.visitedAt)
                .rating(reviewCreateRequest.rating)
                .content(reviewCreateRequest.content)
                .images(images)
                .build();
    }

    private Place convertPlace(PlaceDTO placeDTO){
        return Place.builder()
                .id(placeDTO.getId())
                .contentTypeId(placeDTO.getContentTypeId())
                .title(placeDTO.getTitle())
                .location(convertLocation(placeDTO.getLocationDTO()))
                .thumbnail(placeDTO.getThumbnail())
                .originalImage(placeDTO.getOriginalImage())
                .createdTime(placeDTO.getCreatedTime())
                .modifiedTime(placeDTO.getModifiedTime())
                .gallery(placeDTO.getGallery()) //todo gallery List 확인 필요
                .build();
    }

    private Location convertLocation(LocationDTO locationDTO){
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
