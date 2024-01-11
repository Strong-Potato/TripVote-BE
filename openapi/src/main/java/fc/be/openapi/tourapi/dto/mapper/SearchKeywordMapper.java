package fc.be.openapi.tourapi.dto.mapper;

import fc.be.openapi.tourapi.dto.bone.LocationDTO;
import fc.be.openapi.tourapi.dto.bone.PlaceDTO;
import fc.be.openapi.tourapi.dto.form.same_property.search_keyword1.SearchKeyword1Response;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static fc.be.openapi.config.TourAPIProperties.FORMATTER;

@Component
public class SearchKeywordMapper implements TourAPIMapper<PlaceDTO, SearchKeyword1Response.Item> {

    @Override
    public PlaceDTO generate(SearchKeyword1Response.Item item) {
        return PlaceDTO.builder()
                .id(Integer.parseInt(item.contentid()))
                .contentTypeId(Integer.parseInt(item.contenttypeid()))
                .title(item.title())
                .location(LocationDTO.builder()
                        .address(item.addr1())
                        .addressDetail(item.addr2())
                        .phone(item.tel())
                        .areaCode(Integer.parseInt(item.areacode()))
                        .sigunguCode(Integer.parseInt(item.sigungucode()))
                        .latitude(Double.parseDouble(item.mapy()))
                        .longitude(Double.parseDouble(item.mapx()))
                        .build())
                .category(item.cat3())
                .thumbnail(item.firstimage())
                .originalImage(item.firstimage2())
                .createdTime(LocalDateTime.parse(item.createdtime(), FORMATTER))
                .modifiedTime(LocalDateTime.parse(item.modifiedtime(), FORMATTER))
                .build();
    }
}
