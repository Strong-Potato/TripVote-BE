package fc.be.app.domain.review.dto;

public record RatingStatisticsDTO(
        Double averageRating,
        Long reviewCount
) {
}