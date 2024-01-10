package fc.be.app.domain.place.entity;

import fc.be.app.domain.place.Place;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@SuperBuilder
@Entity
@DiscriminatorValue("Facility")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("문화시설")
public class Facility extends Place {
    @Comment("문의 및 안내")
    String infoCenter;

    @Comment("이용 요금")
    String usefee;

    @Comment("이용 시간")
    private String usetime;

    @Comment("쉬는 날")
    private String restdate;

    @Comment("주차 시설")
    private String parking;

    @SuppressWarnings("unused") // 실제 사용되나 인식 못함
    public void update(Facility facility) {
        super.update(facility);
        this.infoCenter = facility.getInfoCenter();
        this.usefee = facility.getUsefee();
        this.usetime = facility.getUsetime();
        this.restdate = facility.getRestdate();
        this.parking = facility.getParking();
    }
}
