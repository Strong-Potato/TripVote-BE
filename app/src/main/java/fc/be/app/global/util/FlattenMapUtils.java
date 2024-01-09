package fc.be.app.global.util;

import java.util.HashMap;
import java.util.Map;

public class FlattenMapUtils {
    public static Map<String, Object> flattenMap(Map<String, Object> input) {
        return flattenMap(input, "");
    }

    private static Map<String, Object> flattenMap(Map<String, Object> input, String prefix) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            if (entry.getValue() instanceof Map innerMap) {
                result.putAll(flattenMap(innerMap, prefix + entry.getKey() + "_"));
            } else {
                result.put(prefix + entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
}
