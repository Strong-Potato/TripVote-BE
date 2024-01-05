package fc.be.tourapi.dto.form.same_property.detail_common1;


import fc.be.tourapi.dto.form.common.Response;

/**
 * <b>숙박 단건 상세정보(/detailCommon1. 공통정보조회)</b>
 * 타입별공통 정보기본정보,약도이미지,대표이미지,분류정보,지역정보,주소정보,좌표정보,개요정보,길안내정보,이미지정보,연계관광정보목록을 조회하는 기능
 */
public record DetailCommon1Response(Response<Item> response) {
    public record Item(
            String contentid,//콘텐츠 ID
            String contenttypeid,//콘텐츠 타입 ID
            String title,//제목
            String createdtime,//등록일
            String modifiedtime,//수정일
            String tel,//전화번호
            String telname,//전화번호명
            String homepage,//홈페이지 주소
            String booktour,//?
            String firstimage,//대표이미지(원본)
            String firstimage2,//대표이미지(썸네일)
            String cpyrhtDivCd,//저작권자
            String areacode,//지역코드
            String sigungucode,//시군구코드
            String cat1,//대분류
            String cat2,//중분류
            String cat3,//소분류
            String addr1,//대표 주소
            String addr2,//부가적인 위치
            String zipcode,//우편번호
            String mapx,//경도
            String mapy,//위도
            String mlevel,//맵레벨
            String overview//개요
    ) {
    }
}
