package fc.be.app.domain.review.unit;

import fc.be.app.domain.place.Location;
import fc.be.app.domain.place.Place;
import fc.be.app.domain.place.entity.Restaurant;
import fc.be.openapi.tourapi.constant.ContentTypeId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface ReviewFixture {
    Map<String, Object> property = Map.ofEntries(
            Map.entry("placeId", 2807380),
            Map.entry("reviewId", 1L),
            Map.entry("contentTypeId", 39),
            Map.entry("title", "감자밭"),
            Map.entry("rating", 5),
            Map.entry("content", "감자가 너무 맛있어요"),
            Map.entry("profileImage", "profileImage"),
            Map.entry("content2", "정말로 감자가 너무너무너무 맛있어요"),
            Map.entry("images", List.of("image1", "image2")),
            Map.entry("visitedAt", LocalDate.now()),
            Map.entry("str_visitedAt", "2024-01-01"),
            Map.entry("isGoogle", false)
    );

    default Place restaurant() {
        return Restaurant.builder()
                .id(3060930)
                .contentTypeId(ContentTypeId.RESTAURANT)
                .title("코코몽키즈랜드 송파점")
                .location(Location.builder()
                        .address("서울특별시 송파구 충민로 66 (문정동)")
                        .addressDetail("가든파이브 NC백화점 송파점 영관 6층")
                        .phone("")
                        .areaCode(1)
                        .sigunguCode(18)
                        .zipCode(5838)
                        .latitude(37.4776672007)
                        .longitude(127.1249768726)
                        .build())
                .thumbnail("http://tong.visitkorea.or.kr/cms/resource/99/3053899_image2_1.jpg")
                .originalImage("http://tong.visitkorea.or.kr/cms/resource/99/3053899_image3_1.jpg")
                .gallery(Arrays.asList(
                        "http://tong.visitkorea.or.kr/cms/resource/01/3053901_image2_1.jpg",
                        "http://tong.visitkorea.or.kr/cms/resource/02/3053902_image2_1.jpg",
                        "http://tong.visitkorea.or.kr/cms/resource/04/3053904_image2_1.jpg",
                        "http://tong.visitkorea.or.kr/cms/resource/06/3053906_image2_1.jpg",
                        "http://tong.visitkorea.or.kr/cms/resource/09/3053909_image2_1.jpg",
                        "http://tong.visitkorea.or.kr/cms/resource/10/3053910_image2_1.jpg",
                        "http://tong.visitkorea.or.kr/cms/resource/12/3053912_image2_1.jpg",
                        "http://tong.visitkorea.or.kr/cms/resource/13/3053913_image2_1.jpg",
                        "http://tong.visitkorea.or.kr/cms/resource/14/3053914_image2_1.jpg",
                        "http://tong.visitkorea.or.kr/cms/resource/00/3053900_image2_1.jpg"))
                .createdTime(LocalDateTime.parse("2023-11-30T12:29:14"))
                .modifiedTime(LocalDateTime.parse("2023-12-06T16:32:00"))
                .firstMenu("")
                .openTime("[ 평일 ] 10:30~20:00 / [ 주말 ] 10:30~20:30")
                .restDate("연중무휴")
                .packing("")
                .parking("있음(4시간 무료, 영수증으로 정산)")
                .build();
    }
}
