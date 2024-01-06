package fc.be.openapi.tourapi.dto.bone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
public class RestaurantDTO extends PlaceDTO {
    private String firstMenu;
    private String openTime;
    private String restDate;
    private String packing;
    private String parking;
}
