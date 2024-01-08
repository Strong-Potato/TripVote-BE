package fc.be.app.domain.place.entity;

import fc.be.domain.place.Place;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@SuperBuilder
@Entity
@DiscriminatorValue("Spot")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("관광명소")
public class Spot extends Place {
    @Comment("문의 및 안내")
    private String infoCenter;

    @Comment("쉬는 날")
    private String restDate;

    @Comment("이용 시간")
    private String useTime;

    @Comment("주차 시설")
    private String parking;

    @SuppressWarnings("unused") // 실제 사용되나 인식 못함
    public void update(Spot spot) {
        super.update(spot);
        this.infoCenter = spot.getInfoCenter();
        this.restDate = spot.getRestDate();
        this.useTime = spot.getUseTime();
        this.parking = spot.getParking();
    }
}
