package fc.be.app.domain.review.dto.response;


import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.place.Place;
import fc.be.app.domain.review.entity.Review;
import fc.be.openapi.tourapi.tools.area.AreaFinder;
import fc.be.openapi.tourapi.tools.category.CategoryFinder;

import java.time.LocalDate;
import java.util.List;

public record ReviewResponse(
        Long id,
        Author author,
        PlaceInfo place,
        LocalDate visitedAt,
        Integer rating,
        String content,
        List<String> images
) {
    public record Author(
            Long id,
            String email,
            String nickname,
            String profile
    ) {
        public static Author of(Member author) {
            return new Author(author.getId(), author.getEmail(), author.getNickname(), author.getProfile());
        }
    }

    public record PlaceInfo(
            Integer id,
            String title,
            String category,
            String thumbnail,
            String area,
            String sigungu
    ) {
        public static PlaceInfo of(Place place) {
            return new PlaceInfo(
                    place.getId(),
                    place.getTitle(),
                    CategoryFinder.getCategoryByCode(place.getCategory()),
                    place.getThumbnail(),
                    AreaFinder.getCityName(place.getLocation().getAreaCode()),
                    AreaFinder.getDistrictName(place.getLocation().getSigunguCode())
            );
        }
    }

    public static ReviewResponse of(Review review) {
        return new ReviewResponse(
                review.getId(),
                Author.of(review.getMember()),
                PlaceInfo.of(review.getPlace()),
                review.getVisitedAt(),
                review.getRating(),
                review.getContent(),
                review.getImages()
        );
    }
}
