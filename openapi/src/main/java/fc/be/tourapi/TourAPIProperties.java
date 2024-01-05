package fc.be.tourapi;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TourAPIProperties {

    public static final DateTimeFormatter formattter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private static int keyIndex = 0;

    public static final String BASE_URL = "https://apis.data.go.kr/B551011/KorService1/";
    public static final String AREA_BASED_SYNC_LIST = "areaBasedSyncList1";
    public static final String DETAIL_COMMON = "detailCommon1";
    public static final String DETAIL_IMAGE = "detailImage1";
    public static final String DETAIL_INTRO = "detailIntro1";
    public static final String SEARCH_KEYWORD = "searchKeyword1";
    public static final String MOBILE_OS = "ETC";
    public static final String MOBILE_APP = "TripVote";
    public static final String RENDER_TYPE = "json";

    public static final String SPOT_STR = "12";//12:관광지
    public static final String FACILITY_STR = "14";//14:문화시설
    public static final String FESTIVAL_STR = "15";//15:축제공연행사
    public static final String LEPORTS_STR = "28";//28:레포츠
    public static final String ACCOMMODATION_STR = "32";//32:숙박
    public static final String SHOP_STR = "38";//38:쇼핑
    public static final String RESTAURANT_STR = "39";//39:음식점

    private static final List<String> keyList = IntStream.rangeClosed(1, 5)
            .mapToObj(i -> System.getenv("TOUR_API_KEY_" + i))
            .toList();

    public static String getEncodedKey() {
        return keyList.get(keyIndex);
    }

    public static void changeNextKey() {
        log.info("API KEY를 변경합니다.");
        TourAPIProperties.keyIndex = ++keyIndex % keyList.size();
    }
}
