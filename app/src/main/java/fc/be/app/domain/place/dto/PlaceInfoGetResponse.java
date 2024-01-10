package fc.be.app.domain.place.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fc.be.app.domain.place.Place;
import fc.be.app.domain.place.entity.*;
import fc.be.openapi.tourapi.constant.ContentTypeId;
import fc.be.openapi.tourapi.dto.bone.*;

import java.io.IOException;
import java.util.Map;

public record PlaceInfoGetResponse(PlaceDTO place) {
    private static final ObjectMapper objectMapper;
    private static final SimpleModule module;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        module = new SimpleModule();
        module.addDeserializer(ContentTypeId.class, new JsonDeserializer<>() {
            @Override
            public ContentTypeId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                int value = p.getIntValue();
                return ContentTypeId.of(value);
            }
        });
        objectMapper.registerModule(module);
    }

    private static final Map<Class<? extends PlaceDTO>, Class<? extends Place>> placeMapping = Map.of(
            AccommodationDTO.class, Accommodation.class,
            FacilityDTO.class, Facility.class,
            FestivalDTO.class, Festival.class,
            LeportsDTO.class, Festival.class,
            RestaurantDTO.class, Restaurant.class,
            ShopDTO.class, Shop.class,
            SpotDTO.class, Spot.class
    );

    public Place toPlace() {
        Class<? extends Place> placeClass = placeMapping.get(place.getClass());
        if (placeClass == null) {
            throw new IllegalStateException("Place에 없는 아이템입니다" + place);
        }
        return objectMapper.convertValue(place, placeClass);
    }

}
