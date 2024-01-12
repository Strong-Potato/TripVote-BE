package fc.be.openapi.google.dto.review.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthorAttribution(
        String displayName,
        String photoUri
) {
}
