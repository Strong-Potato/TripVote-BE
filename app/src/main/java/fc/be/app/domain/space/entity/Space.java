package fc.be.app.domain.space.entity;

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
}
