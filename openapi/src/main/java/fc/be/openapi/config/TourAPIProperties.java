package fc.be.openapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "tourapi")
public class TourAPIProperties {

    public static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("[yyyyMMddHHmmss]")
            .appendPattern("[yyyyMMddHHmm]")
            .appendPattern("[yyyyMMddHH]")
            .appendPattern("[yyyyMMdd]")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    private String baseUrl;
    private String areaBasedSyncList;
    private String detailCommon;
    private String detailImage;
    private String detailIntro;
    private String searchKeyword;
    private String mobileOs;
    private String mobileApp;
    private String renderType;

    public static final String SPOT_STR = "12";//12:관광지
    public static final String FACILITY_STR = "14";//14:문화시설
    public static final String FESTIVAL_STR = "15";//15:축제공연행사
    public static final String LEPORTS_STR = "28";//28:레포츠
    public static final String ACCOMMODATION_STR = "32";//32:숙박
    public static final String SHOP_STR = "38";//38:쇼핑
    public static final String RESTAURANT_STR = "39";//39:음식점

}
