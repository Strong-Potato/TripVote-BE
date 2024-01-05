package fc.be.openapi.tourapi.dto.bone;

import fc.be.openapi.tourapi.constant.ContentTypeId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
public class PlaceDTO {
    private Integer id;
    private ContentTypeId contentTypeId;
    private String title;
    private LocationDTO locationDTO;
    private String thumbnail;
    private String originalImage;
    private List<String> gallery;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public void addImageToGallery(String subImage) {
        if (gallery == null) {
            gallery = new ArrayList<>();
        }
        gallery.add(subImage);
    }
}
