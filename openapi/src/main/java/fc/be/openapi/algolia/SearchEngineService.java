package fc.be.openapi.algolia;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.algolia.search.models.indexing.Query;
import com.algolia.search.models.settings.IndexSettings;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchEngineService {

    private final AlgoliaProperties properties;
    private SearchIndex<SearchHistory> searchIndex;

    @PostConstruct
    private void init() throws IOException {
        try (SearchClient client = DefaultSearchClient.create(properties.getApplicationId(), properties.getApiKey())) {
            searchIndex = client.initIndex(properties.getIndexName(), SearchHistory.class);
            searchIndex.setSettings(new IndexSettings()
                    .setRanking(List.of("desc(hitCount)", "desc(modifiedAt)"))
            );
        }
    }

    public void saveSearchHistory(String categoryCode, String categoryName) {
        searchIndex.searchAsync(new Query(categoryCode)).thenAccept(result -> {
            if (result.getHits().isEmpty()) {
                var searchHistory = createSearchHistory(categoryCode, categoryName);
                searchIndex.saveObjectAsync(searchHistory);
            } else {
                SearchHistory history = result.getHits().getFirst();
                searchIndex.partialUpdateObjectAsync(history.updateInfo());
            }
        });
    }

    public Map<String, String> bringPopularCategories(Integer size) {
        var result = searchIndex.search(new Query().setHitsPerPage(size));

        return result.getHits().stream()
                .collect(Collectors.toMap(
                        SearchHistory::categoryCode,
                        SearchHistory::categoryName,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    private static SearchHistory createSearchHistory(String categoryCode, String categoryName) {
        return SearchHistory.builder()
                .objectID(UUID.randomUUID().toString())
                .categoryName(categoryName)
                .categoryCode(categoryCode)
                .hitCount(1)
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}
