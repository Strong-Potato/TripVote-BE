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
@DiscriminatorValue("Restaurant")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Comment("음식점")
public class Restaurant extends Place {
    @Comment("주메뉴")
    private String firstMenu;

    @Comment("오픈 시간")
    private String openTime;

    @Comment("쉬는 날")
    private String restDate;

    @Comment("포장 가능 여부")
    private String packing;

    @Comment("주차 시설")
    private String parking;
}
