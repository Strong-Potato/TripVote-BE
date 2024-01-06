package fc.be.openapi.tourapi.dto.form.diff_property.detail_intro1;

import fc.be.openapi.tourapi.dto.form.common.Response;

/**
 * <b>아이템 단건 부가 정보(/detailIntro. 소개정보조회)</b>
 * 상세소개 쉬는날, 개장기간 등 내역을 조회하는 기능
 */
public record DetailIntro1Response(Response<Item> response) {
}
