package fc.be.openapi.tourapi.dto.response.form.diff_property.detail_intro1.item;

import fc.be.openapi.tourapi.dto.response.form.diff_property.detail_intro1.Item;

public record ShopItemDetailIntro(
        String contentid, // 콘텐츠 ID
        String contenttypeid, // 콘텐츠 타입 ID
        String saleitem, // 판매 품목
        String saleitemcost, // 판매 품목 가격
        String fairday, // 엉업일
        String opendateshopping, // 개장일 (쇼핑)
        String shopguide, // 쇼핑 안내
        String culturecenter, // 문화 센터
        String restroom, // 화장실 정보
        String infocentershopping, // 문의 및 안내 (쇼핑) ★
        String scaleshopping, // 규모 (쇼핑)
        String restdateshopping, // 쉬는 날 (쇼핑) ★
        String parkingshopping, // 주차 시설 (쇼핑) ★
        String chkbabycarriageshopping, // 유모차 대여 여부 (쇼핑)
        String chkpetshopping, // 애완동물 동반 가능 여부 (쇼핑)
        String chkcreditcardshopping, // 신용카드 가능 여부 (쇼핑)
        String opentime // 오픈 시간 ★
) implements Item {
}