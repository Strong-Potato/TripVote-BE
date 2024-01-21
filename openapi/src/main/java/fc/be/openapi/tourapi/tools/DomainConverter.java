package fc.be.openapi.tourapi.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fc.be.openapi.tourapi.constant.ContentTypeId;
import fc.be.openapi.tourapi.dto.mapper.AreaBasedSyncMapper;
import fc.be.openapi.tourapi.dto.mapper.DetailCommonMapper;
import fc.be.openapi.tourapi.dto.mapper.DetailIntroMapper;
import fc.be.openapi.tourapi.dto.mapper.SearchKeywordMapper;
import fc.be.openapi.tourapi.dto.response.bone.*;
import fc.be.openapi.tourapi.dto.response.form.diff_property.detail_intro1.Item;
import fc.be.openapi.tourapi.dto.response.form.same_property.area_based_sync_list1.AreaBasedSyncList1Response;
import fc.be.openapi.tourapi.dto.response.form.same_property.detail_common1.DetailCommon1Response;
import fc.be.openapi.tourapi.dto.response.form.same_property.detail_image1.DetailImage1Response;
import fc.be.openapi.tourapi.dto.response.form.same_property.search_keyword1.SearchKeyword1Response;
import fc.be.openapi.tourapi.exception.TourAPIError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static fc.be.openapi.tourapi.exception.TourAPIErrorCode.JSON_PARSING_ERROR;

@Component
@RequiredArgsConstructor
public class DomainConverter {

    private final DetailCommonMapper detailCommonMapper;
    private final AreaBasedSyncMapper areaBasedSyncMapper;
    private final DetailIntroMapper detailIntroMapper;
    private final SearchKeywordMapper searchKeywordMapper;
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public Class<? extends PlaceDTO> convertPlaceToChildDomain(int contentTypeId) {
        return switch (Objects.requireNonNull(ContentTypeId.of(contentTypeId))) {
            case SPOT -> SpotDTO.class;
            case FACILITY -> FacilityDTO.class;
            case FESTIVAL -> FestivalDTO.class;
            case LEPORTS -> LeportsDTO.class;
            case ACCOMMODATION -> AccommodationDTO.class;
            case SHOP -> ShopDTO.class;
            case RESTAURANT -> RestaurantDTO.class;
        };
    }

    public PlaceDTO fromDetailIntro(Item item, int contentTypeId) {
        var itemClass = convertPlaceToChildDomain(contentTypeId);
        return generateAndCastItem(item, detailIntroMapper::generate, itemClass);
    }

    public PlaceDTO fromDetailCommon(DetailCommon1Response.Item item, int contentTypeId) {
        var itemClass = convertPlaceToChildDomain(contentTypeId);
        return generateAndCastItem(item, detailCommonMapper::generate, itemClass);
    }

    public List<PlaceDTO> fromAreaBasedSyncList(
            List<AreaBasedSyncList1Response.Item> items,
            int contentTypeId
    ) {
        List<PlaceDTO> result = new ArrayList<>();

        for (var item : items) {
            int itemContentTypeId = contentTypeId == 0 ? Integer.parseInt(item.contenttypeid()) : contentTypeId;
            var itemClass = convertPlaceToChildDomain(itemContentTypeId);
            result.add(generateAndCastItem(item, areaBasedSyncMapper::generate, itemClass));
        }
        return result;
    }

    @SuppressWarnings("all")
    public PlaceDTO fromDetailImageList(
            List<DetailImage1Response.Item> items,
            int contentTypeId
    ) {
        var itemClass = convertPlaceToChildDomain(contentTypeId);

        try {
            Constructor<? extends PlaceDTO> constructor = itemClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            PlaceDTO result = constructor.newInstance();
            constructor.setAccessible(false);

            for (var item : items) {
                result.addImageToGallery(item.originimgurl());
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public List<PlaceDTO> fromSearchKeywordList(
            List<SearchKeyword1Response.Item> items,
            int contentTypeId
    ) {
        List<PlaceDTO> result = new ArrayList<>();

        for (var item : items) {
            int itemContentTypeId = contentTypeId == 0 ? Integer.parseInt(item.contenttypeid()) : contentTypeId;
            if (ContentTypeId.contains(itemContentTypeId)) {
                var itemClass = convertPlaceToChildDomain(itemContentTypeId);
                result.add(generateAndCastItem(item, searchKeywordMapper::generate, itemClass));
            }
        }

        return result;
    }

    private <T, U> T generateAndCastItem(U item, Function<U, PlaceDTO> generator, Class<T> itemClass) {
        var generatedItem = generator.apply(item);

        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(generatedItem);
            return objectMapper.readValue(jsonString, itemClass);
        } catch (JsonProcessingException e) {
            new TourAPIError(JSON_PARSING_ERROR);
            return null;
        }
    }


}
