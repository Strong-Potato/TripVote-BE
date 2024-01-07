package fc.be.openapi.tourapi.tools;

import fc.be.openapi.tourapi.exception.TourAPIErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

@Slf4j
public class ErrorMessageLoader {
    private final String errorYamlPath;

    public ErrorMessageLoader(String errorYamlPath) {
        this.errorYamlPath = errorYamlPath;
    }

    public Map<String, Map<String, String>> readErrorMessages() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(errorYamlPath);

        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(inputStream));
        return yaml.load(reader);
    }

    public String getErrorMessage(TourAPIErrorCode errorCode, Object... objs) {
        Map<String, Map<String, String>> errorMessages = readErrorMessages();
        Map<String, String> errorMessage = errorMessages.get(errorCode.name());

        StringJoiner message = new StringJoiner(" > ");

        message.add(errorMessage.get("title"));
        message.add(errorMessage.get("detail"));

        for (Object obj : objs) {
            message.add(obj.toString());
        }
        return message.toString();
    }
}