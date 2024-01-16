package fc.be.openapi.tourapi.dto.mapper;

import fc.be.openapi.tourapi.dto.response.bone.LocationDTO;
import fc.be.openapi.tourapi.dto.response.bone.PlaceDTO;
import fc.be.openapi.tourapi.dto.response.form.same_property.area_based_sync_list1.AreaBasedSyncList1Response;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static fc.be.openapi.config.TourAPIProperties.FORMATTER;

@Component
public class AreaBasedSyncMapper implements TourAPIMapper<PlaceDTO, AreaBasedSyncList1Response.Item> {

    public PlaceDTO generate(AreaBasedSyncList1Response.Item item) {
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