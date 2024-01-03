package fc.be.domain.place;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContentTypeId {
    SPOT(12),
    ACCOMMODATION(32),
    RESTAURANT(39);

    private final int id;
}
