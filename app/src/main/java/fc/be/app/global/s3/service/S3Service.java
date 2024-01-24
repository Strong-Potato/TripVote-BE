package fc.be.app.global.s3.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static fc.be.app.global.s3.service.CreatePreSignedUrlUseCase.PreSignedResponses.PreSignedElement;

@Profile("prod")
@Service
public class S3Service implements CreatePreSignedUrlUseCase {

    private static final long S3_PRESIGNED_TOKEN_EXPIRATION = 30 * 60 * 1000;

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public PreSignedResponses getPreSignedUrl(List<String> imageNames) {
        List<PreSignedElement> elements = new ArrayList<>();

        for (String imageName : imageNames) {
            String fileExtension = extractFileExtension(imageName);
            final String savedFileName = createEncodedFileName() + fileExtension;
            GeneratePresignedUrlRequest generatePresignedUrlRequest = createGeneratePresignedUrlRequest(savedFileName);
            String preSignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
            elements.add(new PreSignedElement(imageName, preSignedUrl));
        }

        return new PreSignedResponses(elements);
    }

    private String extractFileExtension(String imageName) {
        int extensionStartIndex = imageName.lastIndexOf(".");
        return imageName.substring(extensionStartIndex);
    }

    private static String createEncodedFileName() {
        String uuid = UUID.randomUUID().toString();
        return uuid + "_" + LocalDateTime.now();
    }

    private GeneratePresignedUrlRequest createGeneratePresignedUrlRequest(String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPreSignedUrlExpiration());

        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicReadWrite.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += S3_PRESIGNED_TOKEN_EXPIRATION;

        expiration.setTime(expTimeMillis);
        return expiration;
    }
}
