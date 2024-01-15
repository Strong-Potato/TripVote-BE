package fc.be.openapi.tourapi.dto.request;

public record DetailImage1Request(Integer contentId, Integer contentTypeId) implements TourAPIRequest {

    @Override
    public StringBuilder appendQueryStrings(StringBuilder url) {
        url.append("&contentId=").append(contentId);
        url.append("&imageYN=").append("Y");
        url.append("&subImageYN=").append("Y");

        return url;
    }
}
