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
@DiscriminatorValue("Facility")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
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
}
