package fc.be.openapi.tourapi.constant;

import fc.be.openapi.tourapi.exception.TourAPIError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static fc.be.openapi.config.TourAPIProperties.*;
import static fc.be.openapi.tourapi.exception.TourAPIErrorCode.NO_CONTENTTPYEID;
import static java.lang.Integer.parseInt;

@Slf4j
@Getter
@AllArgsConstructor
public enum ContentTypeId {
    SPOT(parseInt(SPOT_STR)),
    FACILITY(parseInt(FACILITY_STR)),
    FESTIVAL(parseInt(FESTIVAL_STR)),
    LEPORTS(parseInt(LEPORTS_STR)),
    ACCOMMODATION(parseInt(ACCOMMODATION_STR)),
    SHOP(parseInt(SHOP_STR)),
    RESTAURANT(parseInt(RESTAURANT_STR));

    private final int id;

    private static final Map<Integer, ContentTypeId> BY_ID = new HashMap<>();

    static {
        for (ContentTypeId e : values()) {
            BY_ID.put(e.id, e);
        }
    }

    public static ContentTypeId of(int id) {
        if (BY_ID.containsKey(id)) {
            return BY_ID.get(id);
        }

        new TourAPIError(NO_CONTENTTPYEID);
        return null;
    }

}
