package fc.be.tripvote.domain.place;

import jakarta.persistence.Embeddable;
import org.hibernate.annotations.Comment;

@Embeddable
public class Location {
    @Comment("주소")
    private String address;

    @Comment("부가 상세 주소(xx건물 n층)")
    private String addressDetail;

    @Comment("전화번호")
    private String phone;

    @Comment("지역 코드")
    private Integer areaCode;

    @Comment("시군구 코드")
    private Integer sigunguCode;

    @Comment("우편번호")
    private Integer zipCode;

    @Comment("위도")
    private Double latitude;

    @Comment("경도")
    private Double longitude;
}
