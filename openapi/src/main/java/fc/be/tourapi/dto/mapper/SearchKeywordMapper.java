package fc.be.tourapi.dto.mapper;

import fc.be.tourapi.constant.ContentTypeId;
import fc.be.domain.place.Location;
import fc.be.domain.place.Place;
import fc.be.tourapi.dto.form.same_property.search_keyword1.SearchKeyword1Response;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static fc.be.config.TourAPIProperties.FORMATTTER;

@Component
public class SearchKeywordMapper implements TourAPIMapper<Place, SearchKeyword1Response.Item> {

    @Override
    public Place generate(SearchKeyword1Response.Item item) {
        return Place.builder()
                .id(Integer.parseInt(item.contentid()))
                .contentTypeId(ContentTypeId.of(Integer.parseInt(item.contenttypeid())))
                .title(item.title())
                .location(Location.builder()
                        .address(item.addr1())
                        .addressDetail(item.addr2())
                        .phone(item.tel())
                        .areaCode(Integer.parseInt(item.areacode()))
                        .sigunguCode(Integer.parseInt(item.sigungucode()))
                        .latitude(Double.parseDouble(item.mapy()))
                        .longitude(Double.parseDouble(item.mapx()))
                        .build())
                .thumbnail(item.firstimage())
                .originalImage(item.firstimage2())
                .createdTime(LocalDateTime.parse(item.createdtime(), FORMATTTER))
                .modifiedTime(LocalDateTime.parse(item.modifiedtime(), FORMATTTER))
                .build();
    }
}
