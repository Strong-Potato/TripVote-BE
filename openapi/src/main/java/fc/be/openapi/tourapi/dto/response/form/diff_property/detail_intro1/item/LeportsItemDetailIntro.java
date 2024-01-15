package fc.be.openapi.tourapi.dto.response.form.diff_property.detail_intro1.item;

import fc.be.openapi.tourapi.dto.response.form.diff_property.detail_intro1.Item;

public record LeportsItemDetailIntro(
        String contentid, // 콘텐츠 ID
        String contenttypeid, // 콘텐츠 타입 ID
        String openperiod, // 개장 기간 ★
        String reservation, // 예약 정보
        String infocenterleports, // 문의 및 안내 (레포츠) ★
        String scaleleports, // 규모 (레포츠)
        String accomcountleports, // 수용 인원 (레포츠)
        String restdateleports, // 쉬는 날 (레포츠) ★
        String usetimeleports, // 이용 시간 (레포츠) ★
        String usefeeleports, // 이용 요금 (레포츠) ★
        String expagerangeleports, // 체험 가능 연령 (레포츠)
        String parkingleports, // 주차 시설 (레포츠) ★
        String parkingfeeleports, // 주차 요금 (레포츠)
        String chkbabycarriageleports, // 유모차 대여 여부 (레포츠)
        String chkpetleports, // 애완동물 동반 가능 여부 (레포츠)
        String chkcreditcardleports // 신용카드 가능 여부 (레포츠)
) implements Item {
}