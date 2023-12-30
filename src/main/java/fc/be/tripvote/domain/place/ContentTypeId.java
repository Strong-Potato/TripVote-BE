package fc.be.tripvote.domain.place;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContentTypeId {
    Spot(12),
    Accommodation(32),
    Restaurant(39);

    private final int id;
}
