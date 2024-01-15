package fc.be.app.domain.member.controller;

import fc.be.app.domain.member.dto.request.CheckTokenRequest;
import fc.be.app.domain.member.dto.request.RegisterMemberRequest;
import fc.be.app.domain.member.dto.request.SendEmailRequest;
import fc.be.app.domain.member.exception.MemberErrorCode;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.service.AuthCommand;
import fc.be.app.domain.member.service.MemberCommand;
import fc.be.app.domain.member.service.MemberCommand.MemberRegisterRequest;
import fc.be.app.domain.member.service.MemberQuery;
import fc.be.app.domain.member.service.MemberQuery.MemberRequest;
import fc.be.app.global.http.ApiResponse;
import fc.be.app.global.mail.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthCommand authCommand;
    private final MemberQuery memberQuery;
    private final MemberCommand memberCommand;
    private final MailService mailService;

    @PostMapping("/register/send-email")
    public ApiResponse sendCodeToEmail(@Valid @RequestBody SendEmailRequest request) {
        String targetEmail = request.email();
        MemberRequest memberRequest = new MemberRequest(targetEmail);
        boolean isExists = memberQuery.exists(memberRequest);
        if (isExists) {
            throw new MemberException(MemberErrorCode.EMAIL_ALREADY_EXISTS);
        }
        String verificationCode = authCommand.generateAndStoreCode(targetEmail);
        mailService.sendSimpleMessage(targetEmail, "토큰 발급합니다잉~", verificationCode);
        return ApiResponse.ok();
    }

    @PostMapping("/register/check-token")
    public ApiResponse verifyEmail(@Valid @RequestBody CheckTokenRequest request) {
        String targetEmail = request.email();
        String verificationCode = request.code();
        String registerToken = authCommand.generateAndStoreToken(targetEmail, verificationCode);
        return ApiResponse.ok(Map.of("token", registerToken));
    }

    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody RegisterMemberRequest request) {
        MemberRegisterRequest memberRegisterRequest =
                new MemberRegisterRequest(request.email(), request.password(), request.nickname(), request.profile());
        authCommand.authenticate(request.email(), request.token());
        memberCommand.register(memberRegisterRequest);
        authCommand.removeRegisterToken(request.token());
        return ApiResponse.ok();
    }
}
