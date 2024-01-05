package fc.be.tourapi.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static fc.be.config.TourAPIProperties.*;

@Getter
@AllArgsConstructor
public enum ContentTypeId {
    SPOT(Integer.parseInt(SPOT_STR)),
    FACILITY(Integer.parseInt(FACILITY_STR)),
    FESTIVAL(Integer.parseInt(FESTIVAL_STR)),
    LEPORTS(Integer.parseInt(LEPORTS_STR)),
    ACCOMMODATION(Integer.parseInt(ACCOMMODATION_STR)),
    SHOP(Integer.parseInt(SHOP_STR)),
    RESTAURANT(Integer.parseInt(RESTAURANT_STR));

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
        throw new IllegalArgumentException("존재하지 않는 ContentTypeId 입니다.");
    }

}
