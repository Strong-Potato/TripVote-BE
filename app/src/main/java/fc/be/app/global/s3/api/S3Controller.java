package fc.be.app.global.s3.api;

import fc.be.app.global.http.ApiResponse;
import fc.be.app.global.s3.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static fc.be.app.global.s3.service.CreatePreSignedUrlUseCase.PreSignedResponses;

@Slf4j
@RequestMapping("/s3")
@RestController
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/presigned")
    public ApiResponse<PreSignedResponses> createPreSigned(@RequestBody List<String> fileNames) {
        log.info(fileNames.toString());
        return ApiResponse.ok(s3Service.getPreSignedUrl(fileNames));
    }
}
