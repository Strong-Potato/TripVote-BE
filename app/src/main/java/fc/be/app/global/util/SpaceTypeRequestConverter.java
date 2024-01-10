package fc.be.app.global.util;

import fc.be.app.domain.space.vo.SpaceType;
import org.springframework.core.convert.converter.Converter;

public class SpaceTypeRequestConverter implements Converter<String, SpaceType> {

    @Override
    public SpaceType convert(String spaceType) {
        if (spaceType == null) {
            return null;
        }

        return SpaceType.of(spaceType);
    }
}
