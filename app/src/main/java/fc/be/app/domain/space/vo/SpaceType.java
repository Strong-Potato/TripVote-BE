package fc.be.app.domain.space.vo;

public enum SpaceType {
    PAST,
    UPCOMING;

    public static SpaceType of(String type) {
        return SpaceType.valueOf(type.toUpperCase());
    }
}
