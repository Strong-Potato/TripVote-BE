package fc.be.openapi.tourapi.dto.mapper;

import fc.be.openapi.tourapi.dto.response.bone.LocationDTO;
import fc.be.openapi.tourapi.dto.response.bone.PlaceDTO;
import fc.be.openapi.tourapi.dto.response.form.same_property.detail_common1.DetailCommon1Response;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static fc.be.openapi.config.TourAPIProperties.FORMATTER;


@Component
public class DetailCommonMapper implements TourAPIMapper<PlaceDTO, DetailCommon1Response.Item> {

    @Override
    public PlaceDTO generate(DetailCommon1Response.Item item) {
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
                        .zipCode(Integer.parseInt(item.zipcode()))
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
