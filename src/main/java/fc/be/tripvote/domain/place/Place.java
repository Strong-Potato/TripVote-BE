package fc.be.tripvote.domain.place;

import fc.be.tripvote.global.util.ContentTypeIdConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Comment("장소")
public abstract class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("장소 id")
    private Long id;

    @Convert(converter = ContentTypeIdConverter.class)
    @Comment("장소 타입 id(숙소, 음식점, 관광명소 등)")
    private ContentTypeId contentTypeId;

    @Comment("장소 이름")
    private String title;

    @Embedded
    private Location location;

    @Comment("검색 결과 제공용 썸네일 이미지")
    private String thumbnail;

    @Comment("장소 정보 대표 이미지")
    private String originalImage;

    @Comment("장소 등록일")
    private LocalDateTime createdTime;

    @Comment("장소 수정일")
    private LocalDateTime modifiedTime;
}
