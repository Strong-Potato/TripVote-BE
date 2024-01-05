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
@DiscriminatorValue("Accommodation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("숙소")
public class Accommodation extends Place {
    @Comment("문의 및 안내")
    private String infoCenter;

    @Comment("체크인 시간")
    private String checkIn;

    @Comment("체크아웃 시간")
    private String checkOut;

    @Comment("예약 URL")
    private String reservationUrl;

    @Comment("주차 시설")
    private String parking;
}
