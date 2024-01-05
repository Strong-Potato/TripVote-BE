package fc.be.domain.place;

import fc.be.tourapi.constant.ContentTypeId;
import fc.be.tourapi.tools.ContentTypeIdConverter;
import fc.be.tourapi.tools.ListImagesConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Comment("장소")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void addImageToGallery(String subImage) {
        gallery.add(subImage);
    }
}
