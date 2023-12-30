package fc.be.tripvote.domain.place.entity;

import fc.be.tripvote.domain.place.Place;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@DiscriminatorValue("Restaurant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("음식점")
public class Restaurant extends Place {
}
