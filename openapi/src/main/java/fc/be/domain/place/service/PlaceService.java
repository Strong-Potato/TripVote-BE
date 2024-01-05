package fc.be.domain.place.service;

import fc.be.domain.place.entity.*;
import fc.be.tourapi.TourAPIService;
import fc.be.tourapi.constant.ContentTypeId;
import fc.be.domain.place.Place;
import fc.be.tourapi.tools.ObjectMerger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final TourAPIService tourAPIService;

    public Place bringPlaceInfo(final int placeId, final int contentTypeId) {
        Class<? extends Place> placeChildClass = getPlaceClass(contentTypeId);

        Place detail = tourAPIService.bringDetailCommonDomain(placeId, contentTypeId, placeChildClass);
        Place images = tourAPIService.bringDetailImageDomains(placeId, placeChildClass);
        Place intro = tourAPIService.bringDetailIntroDomain(placeId, contentTypeId, placeChildClass);

        return ObjectMerger.merge(placeChildClass, detail, images, intro);
    }

    private Class<? extends Place> getPlaceClass(int contentTypeId) {
        return switch (ContentTypeId.of(contentTypeId)) {
            case SPOT -> Spot.class;
            case FACILITY -> Facility.class;
            case FESTIVAL -> Festival.class;
            case LEPORTS -> Leports.class;
            case ACCOMMODATION -> Accommodation.class;
            case SHOP -> Shop.class;
            case RESTAURANT -> Restaurant.class;
        };
    }
}
