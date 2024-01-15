package fc.be.app.global.s3.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fc.be.app.global.s3.service.CreatePreSignedUrlUseCase.PreSignedResponses.PreSignedElement;

@Profile("!prod")
@Service
public class FakeS3Service implements CreatePreSignedUrlUseCase {

    public PreSignedResponses getPreSignedUrl(List<String> imageNames) {
        List<PreSignedElement> elements = new ArrayList<>();

        for (String imageName : imageNames) {
            String uuid = UUID.randomUUID().toString();
            String encodedFileName = uuid + "_" + LocalDateTime.now();

            elements.add(new PreSignedElement(imageName, "https://" + encodedFileName));
        }

        return new PreSignedResponses(elements);
    }
}
