package fc.be.openapi.tourapi.dto.response.form.diff_property.detail_intro1.item;


import fc.be.openapi.tourapi.dto.response.form.diff_property.detail_intro1.Item;

public record AccommodationItemDetailIntro(
        String contentid, // 콘텐츠 ID
        String contenttypeid, // 콘텐츠 타입 ID
        String goodstay, // 굿스테이 여부
        String benikia, // 베니키아 여부
        String hanok, // 한옥 여부
        String roomcount, // 객실 수
        String roomtype, // 객실 타입
        String refundregulation, // 환불 규정
        String checkintime, // 체크인 시간 ★
        String checkouttime, // 체크아웃 시간 ★
        String chkcooking, // 취사 가능 여부
        String seminar, // 세미나실 여부
        String sports, // 스포츠 시설 여부
        String sauna, // 사우나 여부
        String beauty, // 뷰티 시설 여부
        String beverage, // 음료 시설 여부
        String karaoke, // 노래방 여부
        String barbecue, // 바베큐 시설 여부
        String campfire, // 캠프파이어 시설 여부
        String bicycle, // 자전거 대여 여부
        String fitness, // 피트니스 시설 여부
        String publicpc, // 공용 PC 여부
        String publicbath, // 공용 목욕탕 여부
        String subfacility, // 부대 시설
        String foodplace, // 식사 장소
        String reservationurl, // 예약 URL ★
        String pickup, // 픽업 서비스 여부
        String infocenterlodging, // 문의 및 안내 (숙박)
        String parkinglodging, // 주차 시설 (숙박) ★
        String reservationlodging, // 예약 정보 (숙박)
        String scalelodging, // 규모 (숙박)
        String accomcountlodging // 수용 인원 (숙박)
) implements Item {
}