package fc.be.app.domain.vote.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Comment("투표 결과 보기를 누른 회원 목록")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class VoteResultMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long voteId;

    private Long spaceId;

    @Builder
    private VoteResultMember(Long id, Long memberId, Long voteId, Long spaceId) {
        this.id = id;
        this.memberId = memberId;
        this.voteId = voteId;
    }

    public static VoteResultMember of(Long memberId, Long voteId, Long spaceId) {
        return VoteResultMember.builder()
                .memberId(memberId)
                .voteId(voteId)
                .spaceId(spaceId)
                .build();
    }
}
