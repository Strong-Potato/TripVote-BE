package fc.be.app.global.s3.service;

import java.util.List;

public interface CreatePreSignedUrlUseCase {

    PreSignedResponses getPreSignedUrl(List<String> imageNames);

    record PreSignedResponses(
            List<PreSignedElement> elements
    ) {

        public record PreSignedElement(
                String fileName,
                String preSignedUrl
        ) {

        }
    }

}
