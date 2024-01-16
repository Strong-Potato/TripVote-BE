package fc.be.openapi.tourapi.dto.response.form.same_property.detail_image1;


import fc.be.openapi.tourapi.dto.response.form.TourAPIResponse;
import fc.be.openapi.tourapi.dto.response.form.common.Response;
import fc.be.openapi.tourapi.exception.TourAPIError;

import java.util.Collections;
import java.util.List;

import static fc.be.openapi.tourapi.exception.TourAPIErrorCode.NO_ITEMS_FROM_API;

/**
 * <b>콘텐츠에 대한 이미지 상세정보 다건(/detailImage1. 이미지정보조회)</b>
 * 관광정보에 매핑되는 서브이미지목록 및 이미지 자작권 공공누리유형을 조회하는 기능
 */
public record DetailImage1Response(Response<Item> response) implements TourAPIResponse {
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
            String contentid,//콘텐츠 ID
            String originimgurl,//원본 이미지 URL
            String imgname,//이미지 명
            String smallimageurl,//썸네일 이미지 URL
            String cpyrhtDivCd,//이미지 저작권 유형 (Type1:제1유형(출처표시-권장), Type3:제3유형(제1유형+변경금지)
            String serialnum//일련번호
    ) {
    }
}
