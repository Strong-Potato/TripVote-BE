package fc.be.app.global.util;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.List;

public class DelimiterConverter implements AttributeConverter<List<String>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return attribute != null ? String.join(DELIMITER, attribute) : null;
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return dbData != null ? Arrays.stream(dbData.split(DELIMITER)).toList() : null;
    }
}
