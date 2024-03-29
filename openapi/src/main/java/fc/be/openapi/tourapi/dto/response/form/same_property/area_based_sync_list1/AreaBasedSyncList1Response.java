package fc.be.openapi.tourapi.dto.response.form.same_property.area_based_sync_list1;


import fc.be.openapi.tourapi.dto.response.form.TourAPIResponse;
import fc.be.openapi.tourapi.dto.response.form.common.Response;
import fc.be.openapi.tourapi.exception.TourAPIError;

import java.util.Collections;
import java.util.List;

import static fc.be.openapi.tourapi.exception.TourAPIErrorCode.NO_ITEMS_FROM_API;

/**
 * <b>숙박 단건 상세정보(/areaBasedSyncList1, 관광정보 동기화 목록 조회)</b>
 * 지역기반 관광정보파라미터 타입에 따라서 제목순,수정일순,등록일순 정렬검색목록을 조회하는 기능
 */
public record AreaBasedSyncList1Response(Response<Item> response) implements TourAPIResponse {

    @Override
    public boolean isItemEmpty() {
        return response.body().numOfRows() == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Item> getItems() {
        if (isItemEmpty()) {
            new TourAPIError(NO_ITEMS_FROM_API);
            return Collections.emptyList();
        }

        return response().body().items().item();
    }

    public record Item(
            String addr1,//대표 주소
            String addr2,//부가적인 위치
            String areacode,//지역코드
            String booktour,//?
            String cat1,//대분류
            String cat2,//중분류
            String cat3,//소분류
            String contentid,//콘텐츠 ID
            String contenttypeid,//콘텐츠 타입 ID
            String createdtime,//등록일
            String firstimage,//대표이미지(원본)
            String firstimage2,//대표이미지(썸네일)
            String cpyrhtDivCd,//저작권자
            String mapx,//경도
            String mapy,//위도
            String mlevel,//맵레벨
            String modifiedtime,//수정일
            String sigungucode,//시군구코드
            String tel,//전화번호
            String title, //숙소명
            String zipcode,//우편번호
            String showflag
    ) {
    }
}
