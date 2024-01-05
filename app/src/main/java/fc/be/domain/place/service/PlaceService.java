package fc.be.domain.place.service;

import fc.be.tourapi.dto.bone.*;
import fc.be.tourapi.TourAPIService;
import fc.be.tourapi.constant.ContentTypeId;
import fc.be.tourapi.tools.ObjectMerger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final TourAPIService tourAPIService;

    public PlaceDTO bringPlaceInfo(final int placeId, final int contentTypeId) {
        Class<? extends PlaceDTO> placeChildClass = getPlaceClass(contentTypeId);

        PlaceDTO detail = tourAPIService.bringDetailCommonDomain(placeId, contentTypeId, placeChildClass);
        PlaceDTO images = tourAPIService.bringDetailImageDomains(placeId, placeChildClass);
        PlaceDTO intro = tourAPIService.bringDetailIntroDomain(placeId, contentTypeId, placeChildClass);

        return ObjectMerger.merge(placeChildClass, detail, images, intro);
    }

    private Class<? extends PlaceDTO> getPlaceClass(int contentTypeId) {
        return switch (ContentTypeId.of(contentTypeId)) {
            case SPOT -> SpotDTO.class;
            case FACILITY -> FacilityDTO.class;
            case FESTIVAL -> FestivalDTO.class;
            case LEPORTS -> LeportsDTO.class;
            case ACCOMMODATION -> AccommodationDTO.class;
            case SHOP -> ShopDTO.class;
            case RESTAURANT -> RestaurantDTO.class;
        };
    }
}
