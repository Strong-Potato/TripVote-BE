package fc.be.openapi.tourapi.dto.response.bone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
public class FestivalDTO extends PlaceDTO {
    private String sponsor;
    private String sponsorTel;
    private String startDate;
    private String endDate;
    private String playtime;
    private String eventPlace;
    private String homepage;
    private String usetime;
}
