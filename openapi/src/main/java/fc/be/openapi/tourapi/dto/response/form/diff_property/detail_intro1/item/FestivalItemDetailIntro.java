package fc.be.openapi.tourapi.dto.response.form.diff_property.detail_intro1.item;

import fc.be.openapi.tourapi.dto.response.form.diff_property.detail_intro1.Item;

public record FestivalItemDetailIntro(
        String contentid, // 콘텐츠 ID
        String contenttypeid, // 콘텐츠 타입 ID
        String sponsor1, // 주최자1 ★
        String sponsor1tel, // 주최자1 전화번호 ★
        String sponsor2, // 주최자2
        String sponsor2tel, // 주최자2 전화번호
        String eventenddate, // 행사 종료일 ★
        String playtime, // 행사 시간 ★
        String eventplace, // 행사 장소 ★
        String eventhomepage, // 행사 홈페이지 ★
        String agelimit, // 연령 제한
        String bookingplace, // 예약 장소
        String placeinfo, // 장소 정보
        String subevent, // 하위 이벤트
        String program, // 프로그램
        String eventstartdate, // 행사 시작일 ★
        String usetimefestival, // 이용 요금 (페스티벌) ★
        String discountinfofestival, // 할인 정보 (페스티벌)
        String spendtimefestival, // 소요 시간 (페스티벌)
        String festivalgrade // 페스티벌 등급
) implements Item {
}