package fc.be.tourapi.tools.area;

import java.util.List;

public record City(
        String name,
        int areaCode,
        List<District> districts
) {
}