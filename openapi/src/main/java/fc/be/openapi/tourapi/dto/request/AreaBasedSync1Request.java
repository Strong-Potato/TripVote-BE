package fc.be.openapi.tourapi.dto.request;

public record AreaBasedSync1Request(
        Integer pageNo,
        Integer numOfRows,
        Integer areaCode,
        Integer sigunguCode,
        Integer contentTypeId,
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
        url.append(addArrangeIfValid(arrange));
        url.append(addCategoryIfValid(categoryCode));
        url.append(addShowFlag());

        return url;
    }
}
