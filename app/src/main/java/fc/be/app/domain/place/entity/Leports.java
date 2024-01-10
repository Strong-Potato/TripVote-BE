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
@DiscriminatorValue("Leports")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("레포츠")
public class Leports extends Place {
    @Comment("문의 및 안내")
    private String infoCenter;

    @Comment("개장 기간")
    private String openPeriod;

    @Comment("쉬는 날")
    private String restDate;

    @Comment("이용 시간")
    private String useTime;

    @Comment("이용 요금")
    private String useFee;

    @Comment("주차 시설")
    private String parking;

    @SuppressWarnings("unused") // 실제 사용되나 인식 못함
    public void update(Leports leports) {
        super.update(leports);
        this.infoCenter = leports.getInfoCenter();
        this.openPeriod = leports.getOpenPeriod();
        this.restDate = leports.getRestDate();
        this.useTime = leports.getUseTime();
        this.useFee = leports.getUseFee();
        this.parking = leports.getParking();
    }
}
