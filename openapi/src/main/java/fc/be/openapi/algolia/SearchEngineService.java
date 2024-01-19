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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public void saveSearchHistory(String keyword) {
        searchIndex.searchAsync(new Query(keyword)).thenAccept(result -> {
            if (result.getHits().isEmpty()) {
                var searchHistory = createSearchHistory(keyword);
                searchIndex.saveObjectAsync(searchHistory);
            } else {
                SearchHistory history = result.getHits().getFirst();
                searchIndex.partialUpdateObjectAsync(history.updateInfo());
            }
        });
    }

    public List<String> bringPopularSearchKeywords(Integer size) {
        var result = searchIndex.search(new Query().setHitsPerPage(size));

        return new ArrayList<>(result.getHits().stream()
                .map(SearchHistory::keyword)
                .toList()
        );
    }

    private static SearchHistory createSearchHistory(String keyword) {
        return SearchHistory.builder()
                .objectID(UUID.randomUUID().toString())
                .keyword(keyword)
                .hitCount(1)
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}
