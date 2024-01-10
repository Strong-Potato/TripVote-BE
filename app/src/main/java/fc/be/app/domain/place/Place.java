package fc.be.app.domain.place;

import fc.be.app.global.util.ContentTypeIdConverter;
import fc.be.app.global.util.ListImagesConverter;
import fc.be.openapi.tourapi.constant.ContentTypeId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DynamicUpdate
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Comment("장소")
public class Place {

    @Id
    @Comment("장소 id")
    private Integer id;

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

    @Convert(converter = ListImagesConverter.class)
    @Column(length = Short.MAX_VALUE)
    @Comment("장소 정보 추가 진열 이미지")
    private final List<String> gallery = new ArrayList<>();

    @Comment("장소 등록일")
    private LocalDateTime createdTime;

    @Comment("장소 수정일")
    private LocalDateTime modifiedTime;

    public void update(Place place) {
        this.title = place.getTitle();
        this.location = place.getLocation();
        this.thumbnail = place.getThumbnail();
        this.originalImage = place.getOriginalImage();
        this.gallery.clear();
        this.gallery.addAll(place.getGallery());
        this.modifiedTime = place.getModifiedTime();
    }
}
