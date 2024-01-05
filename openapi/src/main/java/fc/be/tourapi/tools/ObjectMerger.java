package fc.be.tourapi.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fc.be.tourapi.dto.bone.PlaceDTO;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ObjectMerger {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static <T extends PlaceDTO> T merge(Class<T> clazz, PlaceDTO target, PlaceDTO... updates) {
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
            return objectMapper.convertValue(targetNode, clazz);
        } catch (ClassCastException e) {
            throw new ClassCastException("병합하는 객체들의 타입이 일치하지 않습니다.");
        }
    }
}