package fc.be.tourapi.dto.bone;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class LeportsDTO extends PlaceDTO {
    private String infoCenter;
    private String openPeriod;
    private String restDate;
    private String useTime;
    private String useFee;
    private String parking;
}
