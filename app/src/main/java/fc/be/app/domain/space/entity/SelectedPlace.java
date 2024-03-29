package fc.be.app.domain.space.entity;

import fc.be.app.domain.place.Place;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("일자별 일정의 상세 장소")
public class SelectedPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("일자별 일정의 상세 장소 id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "place_id")
    @Comment("장소 식별자")
    private Place place;

    @Comment("순서")
    private Integer orders;

    @ManyToOne
    @JoinColumn(name = "journey_id")
    @Comment("일자별 일정 식별자")
    private Journey journey;

    @Builder
    private SelectedPlace(Place place, Integer orders, Journey journey) {
        this.place = place;
        this.orders = orders;
        this.journey = journey;
    }

    public static SelectedPlace create(Place place, Integer orders, Journey journey) {
        return SelectedPlace.builder()
                .place(place)
                .journey(journey)
                .orders(orders)
                .build();
    }

    public void setOrder(int order){
        this.orders = order;
    }
}
