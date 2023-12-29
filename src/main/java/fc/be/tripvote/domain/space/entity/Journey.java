package fc.be.tripvote.domain.space.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
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
}
