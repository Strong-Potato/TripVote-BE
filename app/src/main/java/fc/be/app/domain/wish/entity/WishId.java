package fc.be.app.domain.wish.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WishId implements Serializable {

    @EqualsAndHashCode.Include
    private Long memberId;

    @EqualsAndHashCode.Include
    private Integer place;
}