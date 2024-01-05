package fc.be.tourapi.dto.bone;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class RestaurantDTO extends PlaceDTO {
    private String firstMenu;
    private String openTime;
    private String restDate;
    private String packing;
    private String parking;
}
