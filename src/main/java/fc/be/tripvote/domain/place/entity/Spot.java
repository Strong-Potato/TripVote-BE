package fc.be.tripvote.domain.place.entity;

import fc.be.tripvote.domain.place.Place;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@DiscriminatorValue("Spot")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("관광명소")
public class Spot extends Place {
}
