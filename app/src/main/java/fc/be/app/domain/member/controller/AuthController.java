package fc.be.app.domain.member.controller;

import fc.be.app.domain.member.dto.request.*;
import fc.be.app.domain.member.exception.MemberErrorCode;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.service.AuthCommand;
import fc.be.app.domain.member.service.MemberCommand;
import fc.be.app.domain.member.service.MemberCommand.MemberRegisterRequest;
import fc.be.app.domain.member.service.MemberQuery;
import fc.be.app.domain.member.service.MemberQuery.MemberRequest;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.http.ApiResponse;
import fc.be.app.global.mail.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        String verificationCode = authCommand.generateVerifyCode(targetEmail);
        mailService.sendSimpleMessage(targetEmail, "토큰 발급합니다잉~", verificationCode);
        return ApiResponse.ok();
    }

    @PostMapping("/register/check-token")
    public ApiResponse verifyEmail(@Valid @RequestBody CheckTokenRequest request) {
        String targetEmail = request.email();
        String verificationCode = request.code();
        String registerToken = authCommand.generateRegisterToken(targetEmail, verificationCode);
        return ApiResponse.ok(Map.of("token", registerToken));
    }

    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody RegisterMemberRequest request) {
        MemberRegisterRequest memberRegisterRequest =
                new MemberRegisterRequest(request.email(), request.password(), request.nickname(), request.profile());
        authCommand.authenticateRegisterToken(request.email(), request.token());
        memberCommand.register(memberRegisterRequest);
        authCommand.removeRegisterToken(request.token());
        return ApiResponse.ok();
    }

    @PostMapping("/modify/password/check")
    public ApiResponse checkPassword(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody CheckPasswordRequest request) {
        Long id = userPrincipal.id();
        String password = request.password();
        String modifyToken = authCommand.generateModifyToken(id, password);
        return ApiResponse.ok(Map.of("token", modifyToken));
    }

    @PostMapping("/modify/password")
    public ApiResponse changePassword(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody ModifyPasswordRequest request) {
        Long id = userPrincipal.id();
        String modifyToken = request.token();
        String newPassword = request.newPassword();
        authCommand.authenticateModifyToken(id, modifyToken);
        memberCommand.modifyPassword(id, newPassword);
        authCommand.removeModifyToken(id);

        return ApiResponse.ok();
    }

    @PostMapping("/modify/lost-password/send-email")
    public ApiResponse changeLostPassword(@Valid @RequestBody SendEmailRequest request) {
        String targetEmail = request.email();
        MemberRequest memberRequest = new MemberRequest(targetEmail);
        boolean isExists = memberQuery.exists(memberRequest);
        if (!isExists) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        String verificationCode = authCommand.generateVerifyCode(targetEmail);
        mailService.sendSimpleMessage(targetEmail, "토큰 발급합니다잉~", verificationCode);
        return ApiResponse.ok();
    }
}
