package fc.be.tourapi.dto.bone;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class AccommodationDTO extends PlaceDTO {
    private String infoCenter;
    private String checkIn;
    private String checkOut;
    private String reservationUrl;
    private String parking;
}
