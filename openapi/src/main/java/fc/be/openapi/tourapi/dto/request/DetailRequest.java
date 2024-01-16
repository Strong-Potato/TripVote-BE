package fc.be.openapi.tourapi.dto.request;

public record DetailRequest(Integer placeId, Integer placeTypeId) {
    public DetailCommon1Request toDetailCommon() {
        return new DetailCommon1Request(placeId, placeTypeId);
    }

    public DetailImage1Request toDetailImage() {
        return new DetailImage1Request(placeId, placeTypeId);
    }

    public DetailIntro1Request toDetailIntro() {
        return new DetailIntro1Request(placeId, placeTypeId);
    }
}
