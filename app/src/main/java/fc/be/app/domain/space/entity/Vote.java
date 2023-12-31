package fc.be.app.domain.space.entity;

import fc.be.app.domain.space.vo.VoteStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = false, of = {"id"})
@Comment("투표")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("투표 id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "space_id")
    @Comment("여행스페이스 식별자")
    private Space space;

    @Comment("투표명")
    private String title;

    @Enumerated(EnumType.STRING)
    @Comment("투표상태")
    private VoteStatus status;

    @OneToMany(mappedBy = "vote")
    @Comment("후보지")
    private List<Candidate> candidates;
}
