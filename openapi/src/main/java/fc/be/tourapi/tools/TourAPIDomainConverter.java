package fc.be.tourapi.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fc.be.domain.place.Place;
import fc.be.tourapi.dto.form.diff_property.detail_intro1.Item;
import fc.be.tourapi.dto.form.same_property.area_based_sync_list1.AreaBasedSyncList1Response;
import fc.be.tourapi.dto.form.same_property.detail_common1.DetailCommon1Response;
import fc.be.tourapi.dto.form.same_property.detail_image1.DetailImage1Response;
import fc.be.tourapi.dto.form.same_property.search_keyword1.SearchKeyword1Response;
import fc.be.tourapi.dto.mapper.AreaBasedSyncMapper;
import fc.be.tourapi.dto.mapper.DetailCommonMapper;
import fc.be.tourapi.dto.mapper.DetailIntroMapper;
import fc.be.tourapi.dto.mapper.SearchKeywordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class TourAPIDomainConverter {

    private final DetailCommonMapper detailCommonMapper;
    private final AreaBasedSyncMapper areaBasedSyncMapper;
    private final DetailIntroMapper detailIntroMapper;
    private final SearchKeywordMapper searchKeywordMapper;
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public <T> T buildDetailIntroFromItem(Item item, Class<T> itemClass) {
        return generateAndCastItem(item, detailIntroMapper::generate, itemClass);
    }

    public <T> T buildDetailCommonFromItem(DetailCommon1Response.Item item, Class<T> itemClass) {
        return generateAndCastItem(item, detailCommonMapper::generate, itemClass);
    }

    public <T> List<T> buildAreaBasedSyncListFromItem(
            List<AreaBasedSyncList1Response.Item> items,
            Class<T> itemClass
    ) {
        List<T> result = new ArrayList<>();

        for (var item : items) {
            result.add(generateAndCastItem(item, areaBasedSyncMapper::generate, itemClass));
        }
        return result;
    }

    public <T> T buildDetailImageListFromItem(
            List<DetailImage1Response.Item> items,
            final Class<T> detailImageListClass
    ) {
        try {
            T result = detailImageListClass.getDeclaredConstructor().newInstance();
            if (result instanceof Place place) {
                for (var item : items) {
                    place.addImageToGallery(item.originimgurl());
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> buildSearchKeywordListFromItem(
            List<SearchKeyword1Response.Item> items,
            Class<T> itemClass
    ) {
        List<T> result = new ArrayList<>();

        for (var item : items) {
            result.add(generateAndCastItem(item, searchKeywordMapper::generate, itemClass));
        }

        return result;
    }

    private <T, U> T generateAndCastItem(U item, Function<U, Place> generator, Class<T> itemClass) {
        var generatedItem = generator.apply(item);

        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(generatedItem);
            return objectMapper.readValue(jsonString, itemClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
