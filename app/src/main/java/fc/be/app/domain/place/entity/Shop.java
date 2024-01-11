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
@DiscriminatorValue("Shop")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("쇼핑")
public class Shop extends Place {
    @Comment("문의 및 안내")
    private String infoCenter;

    @Comment("쉬는 날")
    private String restDate;

    @Comment("오픈 시간")
    private String openTime;

    @Comment("주차 시설")
    private String parking;

    @SuppressWarnings("unused") // 실제 사용되나 인식 못함
    public void update(Shop shop) {
        super.update(shop);
        this.infoCenter = shop.getInfoCenter();
        this.restDate = shop.getRestDate();
        this.openTime = shop.getOpenTime();
        this.parking = shop.getParking();
    }
}
