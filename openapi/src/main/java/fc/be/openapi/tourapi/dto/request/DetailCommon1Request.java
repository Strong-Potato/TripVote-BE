package fc.be.openapi.tourapi.dto.request;

public record DetailCommon1Request(Integer contentId, Integer contentTypeId) implements TourAPIRequest {
    @Override
    public StringBuilder appendQueryStrings(StringBuilder url) {
        url.append("&contentId=").append(contentId);
        url.append("&contentTypeId=").append(contentTypeId);
        url.append("&defaultYN=").append("Y");
        url.append("&firstImageYN=").append("Y");
        url.append("&areacodeYN=").append("Y");
        url.append("&catcodeYN=").append("Y");
        url.append("&addrinfoYN=").append("Y");
        url.append("&mapinfoYN=").append("Y");
        url.append("&overviewYN=").append("Y");

        return url;
    }
}
