package fc.be.app.domain.space.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CitiesResponse(
        List<CityResponse> cities
) {

    public static CitiesResponse of(List<CityResponse> cities) {
        return CitiesResponse.builder()
                .cities(cities)
                .build();
    }

    @Builder
    public record CityResponse(
            String cityName,
            String imageUrl
    ) {

    }

}
