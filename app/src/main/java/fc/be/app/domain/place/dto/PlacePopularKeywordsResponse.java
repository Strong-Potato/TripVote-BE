package fc.be.app.domain.place.dto;

import java.util.List;
import java.util.Map;

public record PlacePopularKeywordsResponse(List<Category> keywords) {

    public static PlacePopularKeywordsResponse from(Map<String, String> popularKeywords) {
        return new PlacePopularKeywordsResponse(
                popularKeywords.entrySet().stream()
                        .map(entry -> new Category(entry.getKey(), entry.getValue()))
                        .toList()
        );
    }

    private record Category(
            String code,
            String name
    ) {
    }
}
