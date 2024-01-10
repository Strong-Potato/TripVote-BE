package fc.be.app.domain.wish.service;

import fc.be.app.domain.place.Place;
import fc.be.app.domain.place.service.PlaceService;
import fc.be.app.domain.wish.dto.WishAddRequest;
import fc.be.app.domain.wish.dto.WishAddResponse;
import fc.be.app.domain.wish.dto.WishGetResponse;
import fc.be.app.domain.wish.entity.Wish;
import fc.be.app.domain.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishService {
    private final WishRepository wishRepository;
    private final PlaceService placeService;

    @Transactional
    public WishAddResponse addWish(WishAddRequest wishAddRequest) {
        int placeId = wishAddRequest.placeId();
        int contentTypeId = wishAddRequest.contentTypeId();

        Place place = placeService.saveOrUpdatePlace(placeId, contentTypeId);

        Wish wish = Wish.builder()
                .memberId(wishAddRequest.memberId())
                .place(place)
                .build();

        return WishAddResponse.from(wishRepository.save(wish));
    }

    public WishGetResponse getWishes(Long memberId, Pageable pageable) {
        return WishGetResponse.from(wishRepository.findAllByMemberId(memberId, pageable).toList());
    }

    @Transactional
    public boolean deleteWish(Long memberId, Integer placeId) {
        return wishRepository.deleteByMemberIdAndPlaceId(memberId, placeId) > 0;
    }
}
