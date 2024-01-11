package fc.be.openapi.tourapi.dto.bone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    private String address;
    private String addressDetail;
    private String phone;
    private Integer areaCode;
    private Integer sigunguCode;
    private Integer zipCode;
    private Double latitude;
    private Double longitude;
}
