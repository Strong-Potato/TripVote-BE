package fc.be.openapi.tourapi.dto.mapper;

import fc.be.openapi.tourapi.dto.bone.LocationDTO;
import fc.be.openapi.tourapi.dto.bone.PlaceDTO;
import fc.be.openapi.tourapi.dto.form.same_property.area_based_sync_list1.AreaBasedSyncList1Response;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static fc.be.openapi.config.TourAPIProperties.FORMATTTER;

@Component
public class AreaBasedSyncMapper implements TourAPIMapper<PlaceDTO, AreaBasedSyncList1Response.Item> {

    public PlaceDTO generate(AreaBasedSyncList1Response.Item item) {
        return PlaceDTO.builder()
                .id(Integer.parseInt(item.contentid()))
                .contentTypeId(Integer.parseInt(item.contenttypeid()))
                .title(item.title())
                .locationDTO(LocationDTO.builder()
                        .address(item.addr1())
                        .addressDetail(item.addr2())
                        .phone(item.tel())
                        .areaCode(Integer.parseInt(item.areacode()))
                        .sigunguCode(Integer.parseInt(item.sigungucode()))
                        .zipCode(Integer.parseInt(item.zipcode()))
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