package fc.be.tourapi.dto.bone;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class FacilityDTO extends PlaceDTO {
    private String infoCenter;
    private String usefee;
    private String usetime;
    private String restdate;
    private String parking;
}
