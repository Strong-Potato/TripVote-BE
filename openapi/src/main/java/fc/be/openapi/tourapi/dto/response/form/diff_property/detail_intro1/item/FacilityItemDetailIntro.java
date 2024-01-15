package fc.be.openapi.tourapi.dto.response.form.diff_property.detail_intro1.item;

import fc.be.openapi.tourapi.dto.response.form.diff_property.detail_intro1.Item;

public record FacilityItemDetailIntro(
        String contentid, // 콘텐츠 ID
        String contenttypeid, // 콘텐츠 타입 ID
        String scale, // 규모
        String usefee, // 이용요금 ★
        String discountinfo, // 할인 정보
        String spendtime, // 소요 시간
        String parkingfee, // 주차 요금
        String infocenterculture, // 문의 및 안내 ★
        String accomcountculture, // 수용 인원
        String usetimeculture, // 이용 시간 (문화시설) ★
        String restdateculture, // 쉬는 날 (문화시설) ★
        String parkingculture, // 주차 시설 ★
        String chkbabycarriageculture, // 유모차 대여 여부
        String chkpetculture, // 애완동물 동반 가능 여부 ★
        String chkcreditcardculture // 신용카드 가능 여부
) implements Item {
}