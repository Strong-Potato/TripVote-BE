package fc.be.domain.place.entity;

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
@DiscriminatorValue("Leports")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
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
}
