package fc.be.openapi.tourapi.dto.request;

public record SearchKeyword1Request(
        Integer pageNo,
        Integer numOfRows,
        Integer areaCode,
        Integer sigunguCode,
        Integer contentTypeId,
        String keyword,
        Character arrange,
        String categoryCode
) implements TourAPIRequest {

    @Override
    public StringBuilder appendQueryStrings(StringBuilder url) {
        url.append(addPageNoIfValid(pageNo));
        url.append(addNumOfRowsIfValid(numOfRows));
        url.append(addAreaCodeIfValid(areaCode));
        url.append(addSigunguCodeIfValid(sigunguCode));
        url.append(addContentTypeIdIfValid(contentTypeId));
        url.append(addKeywordIfValid(keyword));
        url.append(addArrangeIfValid(arrange));
        url.append(addCategoryIfValid(categoryCode));

        return url;
    }
}
