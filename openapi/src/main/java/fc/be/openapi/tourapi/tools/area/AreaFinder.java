package fc.be.openapi.tourapi.tools.area;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AreaFinder {
    private static final String AREA_JSON_PATH = "/areas.json";

    private static final Map<String, City> CITY_MAP = new HashMap<>();
    private static final Map<Integer, String> AREA_CODE_MAP = new HashMap<>();
    private static final Map<Integer, String> DISTRICT_CODE_MAP = new HashMap<>();

    static {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<City>> typeReference = new TypeReference<>() {
        };
        InputStream inputStream = TypeReference.class.getResourceAsStream(AREA_JSON_PATH);
        try {
            List<City> cities = mapper.readValue(inputStream, typeReference);
            addAreasToMap(cities);
        } catch (IOException e) {
            log.error("행정구역 정보가 담긴 json 파일을 가져오지 못했습니다: {}", e.getMessage());
        }
    }

    private static void addAreasToMap(List<City> cities) {
        for (City city : cities) {
            CITY_MAP.put(city.name(), city);
            AREA_CODE_MAP.put(city.areaCode(), city.name());
            for (District district : city.districts()) {
                DISTRICT_CODE_MAP.put(district.sigunguCode(), district.name());
            }
        }
    }

    public static int getAreaCode(String cityName) {
        City city = CITY_MAP.get(cityName);
        return city != null ? city.areaCode() : -1;
    }

    public static int getSigunguCode(String districtName) {
        for (City city : CITY_MAP.values()) {
            for (District district : city.districts()) {
                if (district.name().equals(districtName)) {
                    return district.sigunguCode();
                }
            }
        }
        return -1;
    }

    public static String getCityName(int areaCode) {
        return AREA_CODE_MAP.getOrDefault(areaCode, null);
    }

    public static String getDistrictName(int districtCode) {
        return DISTRICT_CODE_MAP.getOrDefault(districtCode, null);
    }
}