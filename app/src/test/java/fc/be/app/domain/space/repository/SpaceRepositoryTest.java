package fc.be.app.domain.space.repository;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.space.entity.JoinedMember;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.vo.SpaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@ActiveProfiles("test")
@DataJpaTest
class SpaceRepositoryTest {

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JoinedMemberRepository joinedMemberRepository;

    @DisplayName("주어진 날짜보다 이전 날짜의 여행 스페이스들을 호출한다.")
    @Test
    void findByEndDatePast() {
        // given
        Space space1 = createSpace(LocalDate.of(2024, 1, 7), LocalDate.of(2024, 1, 10));
        Space space2 = createSpace(LocalDate.of(2024, 1, 5), LocalDate.of(2024, 1, 7));
        Space space3 = createSpace(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 3));

        spaceRepository.saveAll(List.of(space1, space2, space3));

        Member member = Member.builder()
            .nickname("tester")
            .build();

        Member savedMember = memberRepository.save(member);

        JoinedMember joinedMember1 = createJoinedMember(space1, member);
        JoinedMember joinedMember2 = createJoinedMember(space2, member);
        JoinedMember joinedMember3 = createJoinedMember(space3, member);

        joinedMemberRepository.saveAll(List.of(joinedMember1, joinedMember2, joinedMember3));

        // when
        List<Space> spaces = spaceRepository.findByEndDateAndMember(
            LocalDate.of(2024, 1, 8), savedMember.getId(), SpaceType.PAST);

        // then
        assertThat(spaces).hasSize(2)
            .extracting("startDate", "endDate")
            .containsExactlyInAnyOrder(
                tuple(space2.getStartDate(), space2.getEndDate()),
                tuple(space3.getStartDate(), space3.getEndDate())
            );
    }

    @DisplayName("기준일 기준하에 지난간 일정을 조회한다.")
    @Test
    void findByEndDateUpcoming() {
        // given
        Space space1 = createSpace(LocalDate.of(2024, 1, 7), LocalDate.of(2024, 1, 10));
        Space space2 = createSpace(LocalDate.of(2024, 1, 7), LocalDate.of(2024, 1, 8));
        Space space3 = createSpace(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 3));

        spaceRepository.saveAll(List.of(space1, space2, space3));

        Member member = Member.builder()
            .nickname("tester")
            .build();

        Member savedMember = memberRepository.save(member);

        JoinedMember joinedMember1 = createJoinedMember(space1, member);
        JoinedMember joinedMember2 = createJoinedMember(space2, member);
        JoinedMember joinedMember3 = createJoinedMember(space3, member);

        joinedMemberRepository.saveAll(List.of(joinedMember1, joinedMember2, joinedMember3));

        // when
        List<Space> spaces = spaceRepository.findByEndDateAndMember(
            LocalDate.of(2024, 1, 8), savedMember.getId(), SpaceType.UPCOMING);

        // then
        assertThat(spaces).hasSize(2)
            .extracting("startDate", "endDate")
            .containsExactlyInAnyOrder(
                tuple(space2.getStartDate(), space2.getEndDate()),
                tuple(space1.getStartDate(), space1.getEndDate())
            );
    }

    private Space createSpace(LocalDate startDate, LocalDate endDate) {
        return Space.builder()
            .title("부산 여행")
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }

    private JoinedMember createJoinedMember(Space space1, Member member) {
        return JoinedMember.builder()
            .space(space1)
            .member(member)
            .build();
    }
}
