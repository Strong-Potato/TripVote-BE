package fc.be.openapi.google;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchJsonReader {
    public String readFirstPlaceId(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        String firstPlaceId = null;
        try (InputStream inputStream = SearchJsonReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                JsonNode rootNode = objectMapper.readTree(inputStream);
                JsonNode placesNode = rootNode.get("places");
                if (placesNode.isArray() && !placesNode.isEmpty()) {
                    JsonNode firstPlaceNode = placesNode.get(0);
                    JsonNode idNode = firstPlaceNode.get("id");
                    if (idNode != null) {
                        firstPlaceId = idNode.asText();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return firstPlaceId;
    }
}