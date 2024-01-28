package fc.be.app.domain.space.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.notification.domain.event.space.SpaceEvent;
import fc.be.app.domain.notification.domain.event.vo.MemberEventInfo;
import fc.be.app.domain.notification.domain.event.vo.SpaceEventInfo;
import fc.be.app.domain.notification.entity.NotificationType;
import fc.be.app.domain.place.Place;
import fc.be.app.domain.place.exception.PlaceException;
import fc.be.app.domain.place.repository.PlaceRepository;
import fc.be.app.domain.space.dto.request.*;
import fc.be.app.domain.space.dto.response.*;
import fc.be.app.domain.space.entity.JoinedMember;
import fc.be.app.domain.space.entity.Journey;
import fc.be.app.domain.space.entity.SelectedPlace;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.exception.SpaceException;
import fc.be.app.domain.space.repository.JoinedMemberRepository;
import fc.be.app.domain.space.repository.JourneyRepository;
import fc.be.app.domain.space.repository.SelectedPlaceRepository;
import fc.be.app.domain.space.repository.SpaceRepository;
import fc.be.app.domain.space.vo.KoreanCity;
import fc.be.app.domain.space.vo.SpaceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static fc.be.app.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static fc.be.app.domain.place.exception.PlaceErrorCode.PLACE_NOT_LOADED;
import static fc.be.app.domain.space.dto.request.DeletedPlacesRequest.DeletedPlace;
import static fc.be.app.domain.space.exception.SpaceErrorCode.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
@Service
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private final JoinedMemberRepository joinedMemberRepository;
    private final MemberRepository memberRepository;
    private final JourneyRepository journeyRepository;
    private final PlaceRepository placeRepository;
    private final SelectedPlaceRepository selectedPlaceRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public SpaceResponse createSpace(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        if (spaceRepository.countSpaceByJoinedMembers(member, LocalDate.now()) >= 15) {
            throw new SpaceException(SPACE_MAX_COUNT_OVER);
        }

        Space savedSpace = spaceRepository.save(Space.create());

        JoinedMember joinedMember = JoinedMember.create(savedSpace, member);
        joinedMemberRepository.save(joinedMember);

        savedSpace.addJoinedMember(joinedMember);

        return SpaceResponse.of(savedSpace);
    }

    public SpaceResponse getSpaceById(Long spaceId, Long memberId) {
        final Member requestMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        validateJoinedMember(space, requestMember);

        return SpaceResponse.of(space);
    }

    @Transactional
    public SpaceResponse updateSpaceByTitle(Long spaceId, Long memberId, TitleUpdateRequest updateRequest, LocalDate currentDate) {
        final Member requestMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        validateSpace(space, requestMember, currentDate);

        publishSpaceEvent(space, requestMember, NotificationType.SPACE_LOCATION_CHANGED, String.join(",", updateRequest.cities()) + " 여행", "");

        space.updateByCity(updateRequest.cities());
        return SpaceResponse.of(space);
    }

    @Transactional
    public SpaceResponse updateSpaceByDates(Long spaceId, Long memberId, DateUpdateRequest updateRequest, LocalDate currentDate) {
        final Member requestMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        validateSpace(space, requestMember, currentDate);
        String changeDates = updateRequest.startDate().toString() + " - " + updateRequest.endDate().toString();
        publishSpaceEvent(space, requestMember, NotificationType.SPACE_SCHEDULE_CHANGED, null, changeDates);

        if (space.getJourneys().isEmpty()) {
            List<Journey> journeys = Journey.createJourneys(updateRequest.startDate(),
                    updateRequest.endDate(),
                    space);
            journeyRepository.saveAll(journeys);
        } else {
            int originalDays = countDaysBetween(space.getStartDate(), space.getEndDate());
            int newDays = countDaysBetween(updateRequest.startDate(), updateRequest.endDate());

            if (originalDays > newDays) {
                deleteJourneys(updateRequest, space, originalDays - newDays);
            } else if (originalDays < newDays) {
                addJourneys(updateRequest, space, newDays - originalDays);
            } else {
                space.updateJourneys(newDays, updateRequest.startDate());
            }
        }

        space.updateByDates(updateRequest.startDate(), updateRequest.endDate());

        return SpaceResponse.of(space);
    }

    public SpacesResponse findByEndDateAndMember(LocalDate currentDate, Long memberId, SpaceType type, Pageable pageRequest) {
        Page<Space> myPages = spaceRepository.findByEndDateAndMember(currentDate, memberId, type, pageRequest);

        int totalPages = myPages.getTotalPages();
        long totalElements = myPages.getTotalElements();
        int number = myPages.getNumber();
        int size = myPages.getSize();
        boolean first = myPages.isFirst();
        boolean last = myPages.isLast();
        List<SpaceResponse> content = myPages.get().map(SpaceResponse::of).toList();

        return new SpacesResponse(content, number, size, totalPages, totalElements, first, last);
    }

    @Transactional
    public void exitSpace(Long spaceId, Long memberId) {
        final Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        final Member requestMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        JoinedMember joinedMember = joinedMemberRepository.findBySpaceAndMemberAndLeftSpace(space,
                requestMember, false).orElseThrow(() -> new SpaceException(NOT_JOINED_MEMBER));

        publishSpaceEvent(space, requestMember, NotificationType.MEMBER_EXIT);

        joinedMember.updateLeftSpace(true);
    }

    public JourneysResponse getJourneyForSpace(Long spaceId, Long memberId) {
        final Member requestMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        final Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        validateJoinedMember(space, requestMember);

        List<Journey> journeys = journeyRepository.findAllBySpaceOrderByDateAsc(space);

        return JourneysResponse.from(journeys);
    }

    @Transactional
    public JourneyResponse selectedPlacesForSpace(Long spaceId, Long memberId, SelectedPlaceRequest request, LocalDate currentDate) {
        final Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        final Member requestMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        validateSpace(space, requestMember, currentDate);

        Journey journey = journeyRepository.findById(request.journeyId())
                .orElseThrow(() -> new SpaceException(JOURNEY_NOT_FOUND));

        List<SelectedPlace> selectedPlaces = insertSelectedPlace(request, journey);

        journey.addSelectedPlace(selectedPlaces);

        return JourneyResponse.from(journey);
    }

    @Transactional
    public JourneysResponse updatePlacesForSpace(Long spaceId, Long memberId, SelectedPlacesRequest request, LocalDate currentDate) {
        final Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        final Member requestMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        validateSpace(space, requestMember, currentDate);

        List<Journey> journeys = new ArrayList<>();

        for (SelectedPlaceRequest selectedPlaceRequest : request.places()) {
            Journey journey = journeyRepository.findById(selectedPlaceRequest.journeyId())
                    .orElseThrow(() -> new SpaceException(JOURNEY_NOT_FOUND));

            selectedPlaceRepository.deleteByJourney(journey);
            journey.clearSelectedPlace();

            List<SelectedPlace> selectedPlaces = insertSelectedPlace(selectedPlaceRequest, journey);
            journey.addSelectedPlace(selectedPlaces);
            journeys.add(journey);
        }

        return JourneysResponse.from(journeys);
    }

    public CitiesResponse getCities() {
        List<CitiesResponse.CityResponse> cityList = KoreanCity.getCityList();

        return CitiesResponse.of(cityList);
    }

    @Transactional
    public void joinMember(Long spaceId, Long memberId) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        joinedMemberRepository.findBySpaceAndMemberId(space, memberId)
                .ifPresentOrElse(
                        entity -> entity.updateLeftSpace(false),
                        () -> joinedMemberRepository.save(JoinedMember.create(space, member))
                );

        publishSpaceEvent(space, member, NotificationType.MEMBER_INVITED);
    }

    @Transactional
    public void deleteBySelectedPlace(Long spaceId, Long memberId, DeletedPlacesRequest request, LocalDate currentDate) {
        final Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        final Member requestMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        validateSpace(space, requestMember, currentDate);

        List<Long> journeyIds = new ArrayList<>();

        for (DeletedPlace dp : request.places()) {
            journeyIds.add(dp.journeyId());
            selectedPlaceRepository.deleteByIdIn(dp.selectedIds());
        }

        List<Journey> journeys = journeyRepository.findAllByIdIn(journeyIds);

        for (Journey jo : journeys) {
            int order = 1;
            for (SelectedPlace sp : jo.getPlace()) {
                sp.setOrder(order++);
            }
        }
    }

    private void addJourneys(DateUpdateRequest updateRequest, Space space, int daysDiff) {
        List<Journey> journeys = space.findByAddedJourneys(updateRequest.endDate(), daysDiff);
        journeyRepository.saveAll(journeys);
        space.updateJourneys(countDaysBetween(space.getStartDate(), space.getEndDate()), updateRequest.startDate());
    }

    private void deleteJourneys(DateUpdateRequest updateRequest, Space space, int daysDiff) {
        List<Long> journeys = space.findByDeletedJourneyIds(daysDiff);
        selectedPlaceRepository.deleteByJourneyIds(journeys);
        journeyRepository.deleteAllByIdInBatch(journeys);
        space.updateJourneys(countDaysBetween(updateRequest.startDate(), updateRequest.endDate()), updateRequest.startDate());
    }

    private List<SelectedPlace> insertSelectedPlace(SelectedPlaceRequest selectedPlaceRequest, Journey journey) {
        int lastOrder = journey.getPlace().size();

        if (lastOrder + selectedPlaceRequest.placeIds().size() > 30) {
            throw new SpaceException(SELECTED_PLACES_COUNT_OVER);
        }

        List<SelectedPlace> selectedPlaces = new ArrayList<>();

        for (Integer id : selectedPlaceRequest.placeIds()) {
            lastOrder++;
            Place place = placeRepository.findById(id)
                    .orElseThrow(() -> new PlaceException(PLACE_NOT_LOADED));
            selectedPlaces.add(SelectedPlace.create(place, lastOrder, journey));
        }

        return selectedPlaceRepository.saveAll(selectedPlaces);
    }

    private static int countDaysBetween(LocalDate startDate, LocalDate endDate) {
        return Period.between(startDate, endDate).getDays();
    }

    private static void validateSpace(Space space, Member requestMember, LocalDate currentDate) {
        if (space.getStartDate() != null && space.isReadOnly(currentDate)) {
            throw new SpaceException(SPACE_IS_READ_ONLY);
        }

        validateJoinedMember(space, requestMember);
    }

    private static void validateJoinedMember(Space space, Member requestMember) {
        if (!space.isBelong(requestMember)) {
            throw new SpaceException(NOT_JOINED_MEMBER);
        }
    }

    private void publishSpaceEvent(Space space, Member requestMember, NotificationType type, String changeTitle, String changeDates) {
        String title = (space.getCityToString() != null) ? space.getCityToString() + " 여행" : null;
        String oldDates = (space.getStartDate() != null) ? space.getStartDate().toString() + " - " + space.getEndDate().toString() : null;

        if (changeTitle == null) {
            changeTitle = title;
        }
        eventPublisher.publishEvent(
                new SpaceEvent(
                        space.getId(),
                        new MemberEventInfo(requestMember.getId(), requestMember.getNickname(), requestMember.getProfile()),
                        new SpaceEventInfo(space.getId(), changeTitle, title, oldDates, changeDates),
                        type,
                        LocalDateTime.now())
        );
    }

    private void publishSpaceEvent(Space space, Member requestMember, NotificationType type) {
        String title = (space.getCityToString() != null) ? space.getCityToString() + " 여행" : null;

        eventPublisher.publishEvent(new SpaceEvent(space.getId(),
                new MemberEventInfo(requestMember.getId(), requestMember.getNickname(), requestMember.getProfile()),
                new SpaceEventInfo(space.getId(), title, null, null, null),
                type,
                LocalDateTime.now())
        );
    }
}
