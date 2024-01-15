package fc.be.openapi.tourapi.dto.request;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

public interface TourAPIRequest {
    Set<Character> VALID_ARRANGE_VALUES = Set.of('O', 'Q', 'R');
    Map<Integer, String> CATEGORY_MAP = Map.of(
            9, "3=",
            5, "2=",
            3, "1="
    );

    StringBuilder appendQueryStrings(StringBuilder url);

    default String addPageNoIfValid(Integer pageNo) {
        return pageNo > 0 ? "&pageNo=" + pageNo : "";
    }

    default String addNumOfRowsIfValid(Integer numOfRows) {
        return numOfRows > 0 ? "&numOfRows=" + numOfRows : "";
    }

    default String addAreaCodeIfValid(Integer areaCode) {
        return areaCode > 0 ? "&areaCode=" + areaCode : "";
    }

    default String addSigunguCodeIfValid(Integer sigunguCode) {
        return sigunguCode > 0 ? "&sigunguCode=" + sigunguCode : "";
    }

    default String addContentTypeIdIfValid(Integer contentTypeId) {
        return contentTypeId > 0 ? "&contentTypeId=" + contentTypeId : "";
    }

    default String addKeywordIfValid(String keyword) {
        keyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        return keyword != null ? "&keyword=" + keyword : "";
    }

    default String addArrangeIfValid(Character arrange) {
        return VALID_ARRANGE_VALUES.contains(arrange) ? "&arrange=" + arrange : "";
    }

    default String addCategoryIfValid(String categoryCode) {
        if (categoryCode == null) {
            return "";
        }

        return "&cat" + CATEGORY_MAP.getOrDefault(categoryCode.length(), "1=") + categoryCode;
    }

}
