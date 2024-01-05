package fc.be.tourapi.dto.mapper;

import fc.be.domain.place.entity.*;
import fc.be.tourapi.constant.ContentTypeId;
import fc.be.domain.place.Place;
import fc.be.tourapi.dto.form.diff_property.detail_intro1.Item;
import fc.be.tourapi.dto.form.diff_property.detail_intro1.item.*;
import org.springframework.stereotype.Component;

@Component
public class DetailIntroMapper implements TourAPIMapper<Place, Item> {
    @Override
    public Place generate(Item item) {
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

    public Accommodation generate(AccommodationItemDetailIntro item) {
        return Accommodation.builder()
                .id(Integer.parseInt(item.contentid()))
                .contentTypeId(ContentTypeId.of(Integer.parseInt(item.contenttypeid())))
                .infoCenter(item.infocenterlodging())
                .checkIn(item.checkintime())
                .checkOut(item.checkouttime())
                .reservationUrl(item.reservationurl())
                .parking(item.parkinglodging())
                .build();
    }

    public Facility generate(FacilityItemDetailIntro item) {
        return Facility.builder()
                .id(Integer.parseInt(item.contentid()))
                .contentTypeId(ContentTypeId.of(Integer.parseInt(item.contenttypeid())))
                .infoCenter(item.infocenterculture())
                .usefee(item.usefee())
                .usetime(item.usetimeculture())
                .restdate(item.restdateculture())
                .parking(item.parkingculture())
                .build();
    }

    public Festival generate(FestivalItemDetailIntro item) {
        return Festival.builder()
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

    public Leports generate(LeportsItemDetailIntro item) {
        return Leports.builder()
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

    public Restaurant generate(RestaurantItemDetailIntro item) {
        return Restaurant.builder()
                .id(Integer.parseInt(item.contentid()))
                .contentTypeId(ContentTypeId.of(Integer.parseInt(item.contenttypeid())))
                .firstMenu(item.firstmenu())
                .openTime(item.opentimefood())
                .restDate(item.restdatefood())
                .packing(item.packing())
                .parking(item.parkingfood())
                .build();
    }

    public Shop generate(ShopItemDetailIntro item) {
        return Shop.builder()
                .id(Integer.parseInt(item.contentid()))
                .contentTypeId(ContentTypeId.of(Integer.parseInt(item.contenttypeid())))
                .infoCenter(item.infocentershopping())
                .restDate(item.restdateshopping())
                .openTime(item.opentime())
                .parking(item.parkingshopping())
                .build();
    }

    public Spot generate(SpotItemDetailIntro item) {
        return Spot.builder()
                .id(Integer.parseInt(item.contentid()))
                .contentTypeId(ContentTypeId.of(Integer.parseInt(item.contenttypeid())))
                .infoCenter(item.infocenter())
                .restDate(item.restdate())
                .useTime(item.usetime())
                .parking(item.parking())
                .build();
    }
}
