package fc.be.app.domain.space.vo;

import fc.be.app.domain.space.exception.SpaceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static fc.be.app.domain.space.dto.response.CitiesResponse.CityResponse;
import static fc.be.app.domain.space.exception.SpaceErrorCode.NO_SUCH_CITY;

@Getter
@AllArgsConstructor
public enum KoreanCity {

    DOMESTIC("국내", "https://images.unsplash.com/photo-1584664711948-c29ed327a614?q=80&w=3870&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
    GAPYEONG("가평", "https://images.unsplash.com/photo-1644765687004-ab56dcb98dd5?q=80&w=3269&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
    YANGPYEONG("양평", "https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/1jPF/image/VzFSWaNSbdecQ6NU-iP9f7-HzAU.jpg"),
    GANGNEUNG("강릉", "https://images.unsplash.com/photo-1621044332832-717d5d986ab7?q=80&w=1740&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
    SOKCHO("속초", "https://images.unsplash.com/photo-1663949405336-e142646d9fbd?q=80&w=2697&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
    YANGYANG("양양", "https://images.unsplash.com/photo-1591797761976-4c97d23a191e?q=80&w=1760&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
    GYEONGJU("경주", "https://images.unsplash.com/photo-1656980593245-b54c8c0828f0?q=80&w=1740&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
    BUSAN("부산", "https://images.unsplash.com/photo-1578037571214-25e07ed4a487?q=80&w=2808&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
    YEOSU("여수", "https://images.unsplash.com/photo-1651375562199-65caae096ace?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
    SUNGCHEON("순천", "https://images.unsplash.com/photo-1695454247140-859c0047274d?q=80&w=3000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
    INCHEON("인천", "https://images.unsplash.com/photo-1647699351838-05a5c17baac4?q=80&w=1587&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
    JEONJU("전주", "https://images.unsplash.com/photo-1544827503-673e2a2c4c00?w=800&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8JUVDJUEwJTg0JUVDJUEzJUJDfGVufDB8fDB8fHww"),
    JEJU("제주", "https://images.unsplash.com/photo-1579169326371-ccb4e63f7889?q=80&w=1587&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
    CHUNCHEON("춘천", "https://images.unsplash.com/photo-1622628036982-f82aa2fd4fc5?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
    TONGYEONG("통영", "https://images.unsplash.com/photo-1606666476184-7d2940f2db90?q=80&w=2748&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
    GEOJE("거제", "https://images.unsplash.com/photo-1672063937910-d3974ad9bd77?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
    NAMHAE("남해", "https://images.unsplash.com/photo-1607898668108-0aa26c578fe3?q=80&w=2675&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
    ANDONG("안동", "https://images.unsplash.com/photo-1503932860988-56df256a6c5f?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");

    private final String cityName;
    private final String imageUrl;

    public static String getByCityName(String cityName) {
        for (KoreanCity city : values()) {
            if (city.getCityName().equals(cityName)) {
                return city.getImageUrl();
            }
        }
        throw new SpaceException(NO_SUCH_CITY);
    }

    public static List<CityResponse> getCityList() {
        List<CityResponse> list = new ArrayList<>();

        for (KoreanCity city : values()) {
            list.add(CityResponse.builder()
                    .cityName(city.getCityName())
                    .imageUrl(city.getImageUrl())
                    .build());
        }

        return list;
    }


}
