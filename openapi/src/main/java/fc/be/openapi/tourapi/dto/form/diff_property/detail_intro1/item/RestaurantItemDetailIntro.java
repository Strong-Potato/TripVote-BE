package fc.be.openapi.tourapi.dto.form.diff_property.detail_intro1.item;

import fc.be.openapi.tourapi.dto.form.diff_property.detail_intro1.Item;

public record RestaurantItemDetailIntro(
        String contentid, // 콘텐츠 ID
        String contenttypeid, // 콘텐츠 타입 ID
        String seat, // 좌석 정보
        String kidsfacility, // 어린이 시설 여부
        String firstmenu, // 주메뉴 ★
        String treatmenu, // 대표 메뉴
        String smoking, // 흡연 가능 여부
        String packing, // 포장 가능 여부 ★
        String infocenterfood, // 문의 및 안내 (음식점)
        String scalefood, // 규모 (음식점)
        String parkingfood, // 주차 시설 (음식점) ★
        String opendatefood, // 개장일 (음식점)
        String opentimefood, // 오픈 시간 (음식점) ★
        String restdatefood, // 쉬는 날 (음식점) ★
        String discountinfofood, // 할인 정보 (음식점)
        String chkcreditcardfood, // 신용카드 가능 여부 (음식점)
        String reservationfood, // 예약 정보 (음식점)
        String lcnsno // 라이센스 번호
) implements Item {
}
