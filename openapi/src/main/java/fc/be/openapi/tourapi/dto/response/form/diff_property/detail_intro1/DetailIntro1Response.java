package fc.be.openapi.tourapi.dto.response.form.diff_property.detail_intro1;

import fc.be.openapi.tourapi.dto.response.form.TourAPIResponse;
import fc.be.openapi.tourapi.dto.response.form.common.Response;
import fc.be.openapi.tourapi.exception.TourAPIError;

import java.util.Collections;
import java.util.List;

import static fc.be.openapi.tourapi.exception.TourAPIErrorCode.NO_ITEMS_FROM_API;

/**
 * <b>아이템 단건 부가 정보(/detailIntro. 소개정보조회)</b>
 * 상세소개 쉬는날, 개장기간 등 내역을 조회하는 기능
 */
public record DetailIntro1Response(Response<Item> response) implements TourAPIResponse {
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
}
