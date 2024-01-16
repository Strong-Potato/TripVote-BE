package fc.be.openapi.tourapi.dto.request;

public record DetailIntro1Request(Integer contentId, Integer contentTypeId) implements TourAPIRequest {

    @Override
    public StringBuilder appendQueryStrings(StringBuilder url) {
        url.append("&contentId=").append(contentId);
        url.append("&contentTypeId=").append(contentTypeId);

        return url;
    }
}
