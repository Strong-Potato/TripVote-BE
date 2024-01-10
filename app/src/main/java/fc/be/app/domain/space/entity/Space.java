package fc.be.app.domain.space.entity;

import static fc.be.app.domain.space.exception.SpaceErrorCode.INVALID_START_DATE;

import fc.be.app.domain.space.exception.SpaceException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("여행스페이스")
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("여행스페이스 id")
    private Long id;

    @Comment("제목")
    private String title;

    @Comment("시작일")
    private LocalDate startDate;

    @Comment("종료일")
    private LocalDate endDate;

    @OneToMany(mappedBy = "space")
    private List<Journey> journeys;

    @OneToMany(mappedBy = "space")
    private List<Vote> voteSpaces;

    @OneToMany(mappedBy = "space")
    private List<JoinedMember> joinedMembers;

    @Builder
    private Space(String title, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Space create() {
        return Space.builder()
            .build();
    }

    public void updateByTitle(String title) {
        if (title != null && !title.isEmpty()) {
            this.title = title;
        }
    }

    public void updateByDates(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new SpaceException(INVALID_START_DATE);
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
