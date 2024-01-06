package fc.be.openapi.tourapi.dto.form.same_property.search_keyword1;


import fc.be.openapi.tourapi.dto.form.common.Response;

/**
 * <b>숙박 간단 정보 키워드 검색(/searchKeyword1. 키워드 검색 조회)</b>
 * 키워드로 검색을하며 전체별 타입정보별 목록을 조회한다
 */
public record SearchKeyword1Response(Response<Item> response) {
    public record Item(
            String addr1,//대표 주소
            String addr2,//부가적인 위치
            String areacode,//지역코드
            String booktour,//?
            String cat1,//대분류
            String cat2,//중분류
            String cat3,//소분류
            String contentid,//콘텐츠 ID
            String contenttypeid,//콘텐츠 타입 ID
            String createdtime,//등록일
            String firstimage,//대표이미지(원본)
            String firstimage2,//대표이미지(썸네일)
            String cpyrhtDivCd,//저작권자
            String mapx,//경도
            String mapy,//위도
            String mlevel,//맵레벨
            String modifiedtime,//수정일
            String sigungucode,//시군구코드
            String tel,//전화번호
            String title //숙소명
    ) {
    }
}
