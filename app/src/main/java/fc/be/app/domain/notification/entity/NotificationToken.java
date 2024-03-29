package fc.be.app.domain.notification.entity;

import fc.be.app.global.util.ListImagesConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class NotificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("알림 구독에 사용될 유저 토큰")
    @Column(nullable = false)
    private String token;

    @Column(nullable = false, unique = true)
    private Long memberId;

    @Convert(converter = ListImagesConverter.class)
    @Comment("해당 토큰이 구독 중인 토픽")
    private List<String> subscribedTopicIds;

    @Builder
    private NotificationToken(Long id, final String token, final Long memberId) {
        this.id = id;
        this.token = token;
        this.memberId = memberId;
    }

    public static NotificationToken of(String token, Long memberId) {
        return NotificationToken.builder()
                .token(token)
                .memberId(memberId)
                .build();
    }

    public void update(final String token) {
        this.token = token;
    }
}

