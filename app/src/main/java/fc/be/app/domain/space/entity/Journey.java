package fc.be.app.domain.space.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("일자별 일정")
public class Journey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("일자별 일정 id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "space_id")
    @Comment("여행 스페이스 id(FK)")
    private Space space;

    @Comment("일자")
    private LocalDate date;

    @OneToMany(mappedBy = "journey")
    private List<SelectedPlace> place;

    @Builder
    private Journey(Space space, LocalDate date) {
        this.space = space;
        this.date = date;
    }

    public static List<Journey> createJourneys(LocalDate startDate, LocalDate endDate,
                                               Space space) {
        List<Journey> journeyList = new ArrayList<>();

        startDate.datesUntil(endDate.plusDays(1)).forEach(currentDate -> journeyList.add(
                Journey.builder()
                        .date(currentDate)
                        .space(space)
                        .build()
        ));

        return journeyList;
    }

    public void updateDate(LocalDate date) {
        this.date = date;
    }

    public void clearSelectedPlace() {
        place.clear();
    }

    public void addSelectedPlace(List<SelectedPlace> selectedPlaces) {
        this.place.addAll(selectedPlaces);
    }

}
