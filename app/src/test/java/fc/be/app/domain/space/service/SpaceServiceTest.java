package fc.be.app.domain.space.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.space.dto.request.UpdateSpaceRequest.DateUpdateRequest;
import fc.be.app.domain.space.dto.request.UpdateSpaceRequest.TitleUpdateRequest;
import fc.be.app.domain.space.dto.response.SpaceResponse;
import fc.be.app.domain.space.entity.JoinedMember;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.exception.SpaceException;
import fc.be.app.domain.space.repository.JoinedMemberRepository;
import fc.be.app.domain.space.repository.SpaceRepository;
import fc.be.app.domain.space.vo.SpaceType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class SpaceServiceTest {

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private JoinedMemberRepository joinedMemberRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        joinedMemberRepository.deleteAllInBatch();
        spaceRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("여행스페이스 생성 시 빈 값의 신규 여행스페이스를 생성한다.")
    @Test
    void createSpace() {
        // given
        Member member = Member.builder()
            .nickname("tester")
            .build();

        Member savedMember = memberRepository.save(member);

        // when
        SpaceResponse savedSpace = spaceService.createSpace(savedMember.getId());

        // then
        assertThat(savedSpace.getId()).isNotNull();
        assertThat(savedSpace)
            .extracting("title", "startDate", "endDate")
            .containsNull();
    }

    @DisplayName("여행스페이스 id(식별자)로 여행스페이스 값을 가져온다.")
    @Test
    void getSpaceById() {
        // given
        Space space = createSpace(LocalDate.of(2024, 1, 7), LocalDate.of(2024, 1, 10));
        Space savedSpace = spaceRepository.save(space);

        // when
        SpaceResponse target = spaceService.getSpaceById(savedSpace.getId());

        // then
        assertThat(target)
            .extracting("id", "title", "startDate", "endDate")
            .contains(savedSpace.getId(), savedSpace.getTitle(), savedSpace.getStartDate(),
                savedSpace.getEndDate());
    }

    @DisplayName("올바르지 않거나 없는 식별자로 조회 시 예외를 발생시킨다.")
    @Test
    void getSpaceByIdWithNoSpace() {
        // given
        Space space = createSpace(LocalDate.of(2024, 1, 7), LocalDate.of(2024, 1, 10));
        Space savedSpace = spaceRepository.save(space);

        Long spaceId = savedSpace.getId() + 1L;

        // when then
        assertThatThrownBy(() -> spaceService.getSpaceById(spaceId))
            .isInstanceOf(SpaceException.class)
            .hasMessageContaining("여행스페이스 정보가 존재하지 않습니다.");
    }

    @DisplayName("여행 스페이스의 타이틀(title)을 업데이트를 한다.")
    @Test
    void updateSpaceWithTitle() {
        // given
        Space space = createSpace(LocalDate.of(2024, 1, 7), LocalDate.of(2024, 1, 10));
        Space savedSpace = spaceRepository.save(space);

        TitleUpdateRequest updateRequest = TitleUpdateRequest.builder()
            .title("서울 여행")
            .build();

        // when
        SpaceResponse spaceResponse = spaceService.updateSpaceByTitle(savedSpace.getId(),
            updateRequest);

        // then
        assertThat(spaceResponse.getTitle()).isEqualTo(updateRequest.getTitle());
    }

    @DisplayName("여행 스페이스의 시작일자와 종료일자에 대한 값을 업데이트를 한다.")
    @Test
    void updateSpaceWithDates() {
        // given
        Space space = createSpace(LocalDate.of(2024, 1, 7), LocalDate.of(2024, 1, 10));
        Space savedSpace = spaceRepository.save(space);

        DateUpdateRequest updateRequest = DateUpdateRequest.builder()
            .startDate(LocalDate.of(2024, 1, 6))
            .endDate(LocalDate.of(2024, 1, 9))
            .build();

        // when
        SpaceResponse spaceResponse = spaceService.updateSpaceByDates(savedSpace.getId(),
            updateRequest);

        // then
        assertThat(spaceResponse.getStartDate()).isEqualTo(updateRequest.getStartDate());
        assertThat(spaceResponse.getEndDate()).isEqualTo(updateRequest.getEndDate());
    }

    @DisplayName("이미 지난간 시점의 여행 스페이스 리스트를 가져온다.")
    @Test
    void getSpaceListByPast() {
        // given
        Space space1 = createSpace(LocalDate.of(2024, 1, 7), LocalDate.of(2024, 1, 10));
        Space space2 = createSpace(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 7));
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
        List<SpaceResponse> spaceResponses = spaceService.findByEndDateAndMember(
            LocalDate.of(2024, 1, 8), savedMember.getId(), SpaceType.PAST);

        // then
        assertThat(spaceResponses).hasSize(2);
        assertThat(spaceResponses)
            .extracting("title", "startDate", "endDate")
            .containsExactlyInAnyOrder(
                tuple(space2.getTitle(), space2.getStartDate(), space2.getEndDate()),
                tuple(space3.getTitle(), space3.getStartDate(), space3.getEndDate())
            );
    }

    @DisplayName("예정인 여행스페이스들을 조회한다.")
    @Test
    void getSpaceListByUpcoming() {
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
        List<SpaceResponse> spaceResponses = spaceService.findByEndDateAndMember(
            LocalDate.of(2024, 1, 8), savedMember.getId(), SpaceType.UPCOMING);

        // then
        assertThat(spaceResponses).hasSize(2);
        assertThat(spaceResponses)
            .extracting("title", "startDate", "endDate")
            .containsExactlyInAnyOrder(
                tuple(space1.getTitle(), space1.getStartDate(), space1.getEndDate()),
                tuple(space2.getTitle(), space2.getStartDate(), space2.getEndDate())
            );
    }

    private JoinedMember createJoinedMember(Space space1, Member member) {
        return JoinedMember.builder()
            .space(space1)
            .member(member)
            .build();
    }

    private Space createSpace(LocalDate startDate, LocalDate endDate) {
        return Space.builder()
            .title("부산 여행")
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }
}