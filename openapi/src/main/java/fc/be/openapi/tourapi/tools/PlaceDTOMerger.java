package fc.be.openapi.tourapi.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fc.be.openapi.tourapi.dto.response.bone.PlaceDTO;
import fc.be.openapi.tourapi.exception.TourAPIError;
import lombok.NoArgsConstructor;

import static fc.be.openapi.tourapi.exception.TourAPIErrorCode.OBJECT_TYPE_ERROR;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PlaceDTOMerger {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static <T extends PlaceDTO> T merge(PlaceDTO target, PlaceDTO... updates) {
        try {
            ObjectNode targetNode = objectMapper.valueToTree(target);
            for (PlaceDTO update : updates) {
                JsonNode updateNode = objectMapper.valueToTree(update);
                updateNode.fields().forEachRemaining(field -> {
                    if (!field.getValue().isNull()) {
                        targetNode.set(field.getKey(), field.getValue());
                    }
                });
            }
            return objectMapper.convertValue(targetNode, (Class<T>) target.getClass());
        } catch (ClassCastException e) {
            new TourAPIError(OBJECT_TYPE_ERROR);
            return null;
        }
    }
}