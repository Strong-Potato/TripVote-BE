package fc.be.tourapi.dto.form.diff_property.detail_intro1;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fc.be.tourapi.dto.form.diff_property.detail_intro1.item.*;

import static fc.be.config.TourAPIProperties.*;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "contenttypeid",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SpotItemDetailIntro.class, name = SPOT_STR),
        @JsonSubTypes.Type(value = FacilityItemDetailIntro.class, name = FACILITY_STR),
        @JsonSubTypes.Type(value = FestivalItemDetailIntro.class, name = FESTIVAL_STR),
        @JsonSubTypes.Type(value = LeportsItemDetailIntro.class, name = LEPORTS_STR),
        @JsonSubTypes.Type(value = AccommodationItemDetailIntro.class, name = ACCOMMODATION_STR),
        @JsonSubTypes.Type(value = ShopItemDetailIntro.class, name = SHOP_STR),
        @JsonSubTypes.Type(value = RestaurantItemDetailIntro.class, name = RESTAURANT_STR)
})
public interface Item {
    String contentid();

    String contenttypeid();
}