package fc.be.app.domain.place.entity;

import fc.be.domain.place.Place;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@SuperBuilder
@Entity
@DiscriminatorValue("Festival")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("축제")
public class Festival extends Place {
    @Comment("주최지")
    private String sponsor;

    @Comment("주최지 전화번호")
    private String sponsorTel;

    @Comment("행사 시작일")
    private String startDate;

    @Comment("행사 종료일")
    private String endDate;

    @Comment("행사 시작 시간")
    private String playtime;

    @Comment("행사 장소")
    private String eventPlace;

    @Comment("행사 홈페이지")
    private String homepage;

    @Comment("이용 시간")
    private String usetime;

    @SuppressWarnings("unused") // 실제 사용되나 인식 못함
    public void update(Festival festival) {
        super.update(festival);
        this.sponsor = festival.getSponsor();
        this.sponsorTel = festival.getSponsorTel();
        this.startDate = festival.getStartDate();
        this.endDate = festival.getEndDate();
        this.playtime = festival.getPlaytime();
        this.eventPlace = festival.getEventPlace();
        this.homepage = festival.getHomepage();
        this.usetime = festival.getUsetime();
    }
}
