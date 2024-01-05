package fc.be.openapi.tourapi.dto.bone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
public class SpotDTO extends PlaceDTO {
    private String infoCenter;
    private String restDate;
    private String useTime;
    private String parking;
}
