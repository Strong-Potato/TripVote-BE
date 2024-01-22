package fc.be.app.domain.space.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.place.Place;
import fc.be.app.domain.place.exception.PlaceException;
import fc.be.app.domain.place.repository.PlaceRepository;
import fc.be.app.domain.space.dto.request.DateUpdateRequest;
import fc.be.app.domain.space.dto.request.SelectedPlaceRequest;
import fc.be.app.domain.space.dto.request.SelectedPlacesRequest;
import fc.be.app.domain.space.dto.request.TitleUpdateRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static fc.be.app.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static fc.be.app.domain.place.exception.PlaceErrorCode.PLACE_NOT_LOADED;
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

    @Transactional
    public SpaceResponse createSpace(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        if (spaceRepository.countSpaceByJoinedMembers(member) >= 15) {
            throw new SpaceException(SPACE_MAX_COUNT_OVER);
        }

        Space savedSpace = spaceRepository.save(Space.create(member.getNickname()));

        JoinedMember joinedMember = JoinedMember.create(savedSpace, member);
        joinedMemberRepository.save(joinedMember);

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

        JoinedMember joinedMember = joinedMemberRepository.findBySpaceAndMember(space,
                requestMember).orElseThrow(() -> new SpaceException(NOT_JOINED_MEMBER));

        joinedMember.updateLeftSpace(true);
    }

    public JourneysResponse getJourneyForSpace(Long spaceId, Long memberId) {
        final Member requestMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        final Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        validateJoinedMember(space, requestMember);

        List<Journey> journeys = journeyRepository.findAllBySpaceOrderByDateAsc(
                space);

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

        List<SelectedPlace> selectedPlaces = new ArrayList<>();

        for (Integer id : selectedPlaceRequest.selectedPlaces()) {
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

    public CitiesResponse getCities() {
        List<CitiesResponse.CityResponse> cityList = KoreanCity.getCityList();

        return CitiesResponse.of(cityList);
    }

    @Transactional
    public void joinMember(Long spaceId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        JoinedMember joinedMember =
                joinedMemberRepository.findBySpaceAndMember(space, member).orElse(JoinedMember.create(space, member));

        joinedMemberRepository.save(joinedMember);
    }
}
