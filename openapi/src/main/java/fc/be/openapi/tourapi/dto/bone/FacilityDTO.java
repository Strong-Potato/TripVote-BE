package fc.be.openapi.tourapi.dto.bone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
public class FacilityDTO extends PlaceDTO {
    private String infoCenter;
    private String usefee;
    private String usetime;
    private String restdate;
    private String parking;
}
