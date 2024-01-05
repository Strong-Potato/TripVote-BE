package fc.be.tourapi.dto.bone;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LocationDTO {
    private String address;
    private String addressDetail;
    private String phone;
    private Integer areaCode;
    private Integer sigunguCode;
    private Integer zipCode;
    private Double latitude;
    private Double longitude;

    @Builder
    public LocationDTO(String address, String addressDetail, String phone, Integer areaCode, Integer sigunguCode, Integer zipCode, Double latitude, Double longitude) {
        this.address = address;
        this.addressDetail = addressDetail;
        this.phone = phone;
        this.areaCode = areaCode;
        this.sigunguCode = sigunguCode;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
