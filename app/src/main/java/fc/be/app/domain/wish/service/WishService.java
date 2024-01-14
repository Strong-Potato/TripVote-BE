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
import org.springframework.data.domain.Page;
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
        Page<Wish> myWishes = wishRepository.findAllByMemberId(memberId, pageable);

        int totalPages = myWishes.getTotalPages();
        long totalElements = myWishes.getTotalElements();
        int number = myWishes.getNumber();
        int size = myWishes.getSize();
        boolean first = myWishes.isFirst();
        boolean last = myWishes.isLast();

        return WishGetResponse.from(myWishes.getContent(), number, size, totalPages, totalElements, first, last);
    }

    @Transactional
    public boolean deleteWish(Long memberId, Integer placeId) {
        return wishRepository.deleteByMemberIdAndPlaceId(memberId, placeId) > 0;
    }
}
