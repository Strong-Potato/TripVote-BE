package fc.be.openapi.tourapi.tools.category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@UtilityClass
public class CategoryFinder {
    private static final Map<String, String> CATEGORY_MAP = new HashMap<>();

    static {
        String path = URI.create("/categories.json").toString();
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream inputStream = TypeReference.class.getResourceAsStream(path)) {
            List<Category> categories = mapper.readValue(inputStream, new TypeReference<>() {
            });
            for (Category category : categories) {
                CATEGORY_MAP.put(category.code(), category.name());
            }
        } catch (IOException e) {
            log.error("JSON 파일에서 카테고리를 읽어올 수 없습니다", e);
        }
    }

    private record Category(String code, String name) {
    }

    public static String getCategoryByCode(String code) {
        return CATEGORY_MAP.get(code);
    }
}