package fc.be.openapi.tourapi.dto.response.bone;

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
    private Integer contentTypeId;
    private String title;
    private LocationDTO location;
    private String category;
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
