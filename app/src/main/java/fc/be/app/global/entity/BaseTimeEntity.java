package fc.be.app.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false)
    @Comment("생성 시각")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Comment("수정 시각")
    private LocalDateTime modifiedDate;
}
