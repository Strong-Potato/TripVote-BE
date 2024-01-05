package fc.be.tourapi.dto.bone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
public class ShopDTO extends PlaceDTO {
    private String infoCenter;
    private String restDate;
    private String openTime;
    private String parking;
}
