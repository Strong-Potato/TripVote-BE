package fc.be.app.domain.place.entity;

import fc.be.app.domain.place.Place;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @SuppressWarnings("unused") // 실제 사용되나 인식 못함
    public void update(Restaurant restaurant) {
        super.update(restaurant);
        this.firstMenu = restaurant.getFirstMenu();
        this.openTime = restaurant.getOpenTime();
        this.restDate = restaurant.getRestDate();
        this.packing = restaurant.getPacking();
        this.parking = restaurant.getParking();
    }
}
