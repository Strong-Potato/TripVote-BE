package fc.be.openapi.tourapi.dto.mapper;

import fc.be.openapi.tourapi.dto.bone.*;
import fc.be.openapi.tourapi.constant.ContentTypeId;
import fc.be.openapi.tourapi.dto.form.diff_property.detail_intro1.Item;
import fc.be.openapi.tourapi.dto.form.diff_property.detail_intro1.item.*;
import org.springframework.stereotype.Component;

@Component
public class DetailIntroMapper implements TourAPIMapper<PlaceDTO, Item> {
    @Override
    public PlaceDTO generate(Item item) {
        return switch (item) {
            case AccommodationItemDetailIntro accommodation -> generate(accommodation);
            case FacilityItemDetailIntro facility -> generate(facility);
            case FestivalItemDetailIntro festival -> generate(festival);
            case LeportsItemDetailIntro leports -> generate(leports);
            case RestaurantItemDetailIntro restaurant -> generate(restaurant);
            case ShopItemDetailIntro shop -> generate(shop);
            case SpotItemDetailIntro spot -> generate(spot);
            default -> throw new IllegalStateException("등록되지 않은 아이템: " + item.getClass().getSimpleName());
        };
    }

    public AccommodationDTO generate(AccommodationItemDetailIntro item) {
        return AccommodationDTO.builder()
                .id(Integer.parseInt(item.contentid()))
                .contentTypeId(ContentTypeId.of(Integer.parseInt(item.contenttypeid())))
                .infoCenter(item.infocenterlodging())
                .checkIn(item.checkintime())
                .checkOut(item.checkouttime())
                .reservationUrl(item.reservationurl())
                .parking(item.parkinglodging())
                .build();
    }

    public FacilityDTO generate(FacilityItemDetailIntro item) {
        return FacilityDTO.builder()
                .id(Integer.parseInt(item.contentid()))
                .contentTypeId(ContentTypeId.of(Integer.parseInt(item.contenttypeid())))
                .infoCenter(item.infocenterculture())
                .usefee(item.usefee())
                .usetime(item.usetimeculture())
                .restdate(item.restdateculture())
                .parking(item.parkingculture())
                .build();
    }

    public FestivalDTO generate(FestivalItemDetailIntro item) {
        return FestivalDTO.builder()
                .id(Integer.parseInt(item.contentid()))
                .contentTypeId(ContentTypeId.of(Integer.parseInt(item.contenttypeid())))
                .sponsor(item.sponsor1())
                .sponsorTel(item.sponsor1tel())
                .startDate(item.eventstartdate())
                .endDate(item.eventenddate())
                .playtime(item.playtime())
                .eventPlace(item.eventplace())
                .homepage(item.eventhomepage())
                .usetime(item.usetimefestival())
                .build();
    }

    public LeportsDTO generate(LeportsItemDetailIntro item) {
        return LeportsDTO.builder()
                .id(Integer.parseInt(item.contentid()))
                .contentTypeId(ContentTypeId.of(Integer.parseInt(item.contenttypeid())))
                .infoCenter(item.infocenterleports())
                .openPeriod(item.openperiod())
                .restDate(item.restdateleports())
                .useTime(item.usetimeleports())
                .useFee(item.usefeeleports())
                .parking(item.parkingleports())
                .build();
    }

    public RestaurantDTO generate(RestaurantItemDetailIntro item) {
        return RestaurantDTO.builder()
                .id(Integer.parseInt(item.contentid()))
                .contentTypeId(ContentTypeId.of(Integer.parseInt(item.contenttypeid())))
                .firstMenu(item.firstmenu())
                .openTime(item.opentimefood())
                .restDate(item.restdatefood())
                .packing(item.packing())
                .parking(item.parkingfood())
                .build();
    }

    public ShopDTO generate(ShopItemDetailIntro item) {
        return ShopDTO.builder()
                .id(Integer.parseInt(item.contentid()))
                .contentTypeId(ContentTypeId.of(Integer.parseInt(item.contenttypeid())))
                .infoCenter(item.infocentershopping())
                .restDate(item.restdateshopping())
                .openTime(item.opentime())
                .parking(item.parkingshopping())
                .build();
    }

    public SpotDTO generate(SpotItemDetailIntro item) {
        return SpotDTO.builder()
                .id(Integer.parseInt(item.contentid()))
                .contentTypeId(ContentTypeId.of(Integer.parseInt(item.contenttypeid())))
                .infoCenter(item.infocenter())
                .restDate(item.restdate())
                .useTime(item.usetime())
                .parking(item.parking())
                .build();
    }
}
