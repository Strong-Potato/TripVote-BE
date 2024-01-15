package fc.be.openapi.tourapi.dto.response.form.diff_property.detail_intro1.item;

import fc.be.openapi.tourapi.dto.response.form.diff_property.detail_intro1.Item;

public record SpotItemDetailIntro(
        String contentid, // 콘텐츠 ID
        String contenttypeid, // 콘텐츠 타입 ID
        String heritage1, // 문화재 분류 1
        String heritage2, // 문화재 분류 2
        String heritage3, // 문화재 분류 3
        String infocenter, // 문의 및 안내 ★
        String opendate, // 개장일
        String restdate, // 쉬는 날 ★
        String expguide, // 체험 안내
        String expagerange, // 체험 가능 연령
        String accomcount, // 수용 인원
        String useseason, // 이용 가능 계절
        String usetime, // 이용 시간 ★
        String parking, // 주차 시설 ★
        String chkbabycarriage, // 유모차 대여 여부
        String chkpet, // 애완동물 동반 가능 여부
        String chkcreditcard // 신용카드 가능 여부
) implements Item {
}