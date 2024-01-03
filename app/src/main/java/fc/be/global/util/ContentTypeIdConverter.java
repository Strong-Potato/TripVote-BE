package fc.be.global.util;

import fc.be.domain.place.ContentTypeId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ContentTypeIdConverter implements AttributeConverter<ContentTypeId, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ContentTypeId attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getId();
    }

    @Override
    public ContentTypeId convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        for (ContentTypeId contentTypeId : ContentTypeId.values()) {
            if (contentTypeId.getId() == dbData) {
                return contentTypeId;
            }
        }
        throw new IllegalArgumentException("Unknown database value: " + dbData);
    }
}