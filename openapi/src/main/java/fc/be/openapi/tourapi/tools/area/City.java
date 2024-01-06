package fc.be.openapi.tourapi.tools.area;

import java.util.List;

public record City(
        String name,
        int areaCode,
        List<District> districts
) {
}