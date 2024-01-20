package fc.be.app.domain.space.entity;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.space.exception.SpaceException;
import fc.be.app.domain.space.vo.KoreanCity;
import fc.be.app.domain.vote.entity.Vote;
import fc.be.app.global.util.DelimiterConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static fc.be.app.domain.space.exception.SpaceErrorCode.INVALID_START_DATE;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("여행스페이스")
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("여행스페이스 id")
    private Long id;

    @Comment("제목")
    private String title;

    @Comment("시작일")
    private LocalDate startDate;

    @Comment("종료일")
    private LocalDate endDate;

    @Convert(converter = DelimiterConverter.class)
    @Comment("선택된도시리스트")
    private List<String> city;

    @OneToMany(mappedBy = "space")
    private List<Journey> journeys;

    @OneToMany(mappedBy = "space")
    private List<Vote> voteSpaces;

    @OneToMany(mappedBy = "space")
    private List<JoinedMember> joinedMembers = new ArrayList<>();

    @Builder
    private Space(String title, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Space create(String nickname) {
        return Space.builder()
                .title(nickname + "의 여행")
                .build();
    }

    public void updateByTitle(String title) {
        this.title = title;
    }

    public void updateByCity(List<String> cities) {
        this.city = cities;
    }

    public void updateByDates(LocalDate startDate, LocalDate endDate) {
        validationDates(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public List<Long> findByDeletedJourneyIds(int daysToRemove) {
        List<Long> journeyIds = new ArrayList<>();

        for (int i = 0; i < daysToRemove; i++) {
            int journeysIndex = journeys.size() - 1 - i;
            journeyIds.add(this.journeys.get(journeysIndex).getId());
        }

        return journeyIds;
    }

    public List<Journey> findByAddedJourneys(LocalDate endDate,
                                             int daysToAdd) {
        List<Journey> journeyList = new ArrayList<>();

        for (int i = 0; i < daysToAdd; i++) {
            journeyList.add(
                    Journey.builder()
                            .date(endDate.minusDays(i))
                            .space(this)
                            .build()
            );
        }

        return journeyList;
    }

    public void updateJourneys(int day, LocalDate startDate) {
        IntStream.rangeClosed(0, day)
                .forEach(index -> journeys.get(index).updateDate(startDate.plusDays(index)));
    }

    private void validationDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new SpaceException(INVALID_START_DATE);
        }
    }

    public boolean isReadOnly(LocalDate requestDate) {
        return requestDate.isAfter(this.endDate);
    }

    public boolean isBelong(Member member) {
        return this.joinedMembers
                .stream()
                .anyMatch(joinedMember -> !joinedMember.isLeftSpace() && joinedMember.getMember().equals(member));
    }

    public String getCityThumbnail() {
        if (this.city == null || this.city.isEmpty()) {
            return null;
        }

        return KoreanCity.getByCityName(this.city.get(0));
    }

}
