package fc.be.app.common.authentication.controller;

import fc.be.app.common.authentication.controller.dto.request.*;
import fc.be.app.common.authentication.controller.dto.response.CodeResponse;
import fc.be.app.common.authentication.controller.dto.response.TokenResponse;
import fc.be.app.common.authentication.exception.AuthErrorCode;
import fc.be.app.common.authentication.exception.AuthException;
import fc.be.app.common.authentication.manager.DelegatingTokenManager;
import fc.be.app.common.authentication.model.ModifyToken;
import fc.be.app.common.authentication.model.RegisterToken;
import fc.be.app.common.authentication.model.Token;
import fc.be.app.common.authentication.service.VerifyService;
import fc.be.app.domain.member.exception.MemberErrorCode;
import fc.be.app.domain.member.exception.MemberException;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final VerifyService verifyService;
    private final DelegatingTokenManager delegatingTokenManager;
    private final MemberQuery memberQuery;
    private final MemberCommand memberCommand;
    private final MailService mailService;

    @PostMapping("/register/send-email")
    public ApiResponse<Void> sendCodeToEmail(@Valid @RequestBody SendEmailRequest request) {
        String targetEmail = request.email();
        MemberRequest memberRequest = new MemberRequest(targetEmail);
        boolean isExists = memberQuery.exists(memberRequest);
        if (isExists) {
            throw new MemberException(MemberErrorCode.EMAIL_ALREADY_EXISTS);
        }
        String verificationCode = verifyService.issueCode(VerifyService.Purpose.EMAIL, targetEmail);
        mailService.sendVerificationCode(targetEmail, "[트립보트] 이메일 인증을 해주세요", verificationCode);
        return ApiResponse.ok();
    }

    @PostMapping("/register/check-token")
    public ApiResponse<TokenResponse> verifyEmail(@Valid @RequestBody CheckTokenRequest request) {
        String targetEmail = request.email();
        String verificationCode = request.code();
        verifyService.verify(VerifyService.Purpose.EMAIL, targetEmail, verificationCode);
        RegisterToken genRequest = RegisterToken.unauthenticated(null, targetEmail);
        Token generatedToken = delegatingTokenManager.generate(genRequest);
        return ApiResponse.ok(new TokenResponse(generatedToken.getTokenValue()));
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterMemberRequest request) {
        String tokenValue = request.token();
        String targetEmail = request.email();
        RegisterToken authRequest = RegisterToken.unauthenticated(tokenValue, targetEmail);
        delegatingTokenManager.authenticate(authRequest);
        MemberRegisterRequest memberRegisterRequest =
                new MemberRegisterRequest(request.email(), request.password(), request.nickname(), request.profile());
        memberCommand.register(memberRegisterRequest);
        delegatingTokenManager.remove(authRequest);
        return ApiResponse.ok();
    }

    @PostMapping("/modify/password/check")
    public ApiResponse<TokenResponse> checkPassword(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody CheckPasswordRequest request) {
        Long targetId = userPrincipal.id();
        String currentPassword = request.password();
        boolean matches = memberQuery.verify(targetId, currentPassword);
        if (!matches) {
            throw new AuthException(AuthErrorCode.INCORRECT_PASSWORD);
        }
        ModifyToken genRequest = ModifyToken.unauthenticated(null, targetId, ModifyToken.AuthenticationStrategy.ID);
        Token generatedToken = delegatingTokenManager.generate(genRequest);
        return ApiResponse.ok(new TokenResponse(generatedToken.getTokenValue()));
    }

    @PostMapping("/modify/password")
    public ApiResponse<Void> changePassword(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody ModifyPasswordRequest request) {
        Long targetId = userPrincipal.id();
        String tokenValue = request.token();
        String newPassword = request.newPassword();
        ModifyToken authRequest = ModifyToken.unauthenticated(tokenValue, String.valueOf(targetId), ModifyToken.AuthenticationStrategy.ID);
        delegatingTokenManager.authenticate(authRequest);
        memberCommand.modifyPassword(targetId, newPassword);
        delegatingTokenManager.remove(authRequest);
        return ApiResponse.ok();
    }

    @PostMapping("/modify/lost-password/send-email")
    public ApiResponse<Void> changeLostPassword(@Valid @RequestBody SendEmailRequest request) {
        String targetEmail = request.email();
        MemberRequest memberRequest = new MemberRequest(targetEmail);
        boolean isExists = memberQuery.exists(memberRequest);
        if (!isExists) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        String verificationCode = verifyService.issueCode(VerifyService.Purpose.EMAIL, targetEmail);
        mailService.sendVerificationCode(targetEmail, "[트립보트] 이메일 인증을 해주세요", verificationCode);
        return ApiResponse.ok();
    }

    @PostMapping("/modify/lost-password/check-token")
    public ApiResponse<TokenResponse> changeLostPassword(@Valid @RequestBody CheckTokenRequest request) {
        String verificationCode = request.code();
        String targetEmail = request.email();
        verifyService.verify(VerifyService.Purpose.EMAIL, targetEmail, verificationCode);
        ModifyToken genRequest = ModifyToken.unauthenticated(null, targetEmail, ModifyToken.AuthenticationStrategy.EMAIL);
        Token generatedToken = delegatingTokenManager.generate(genRequest);
        return ApiResponse.ok(new TokenResponse(generatedToken.getTokenValue()));
    }

    @PostMapping("/modify/lost-password")
    public ApiResponse<Void> changeLostPassword(@Valid @RequestBody ModifyLostPasswordRequest request) {
        String tokenValue = request.token();
        String targetEmail = request.email();
        String newPassword = request.newPassword();
        ModifyToken authRequest = ModifyToken.unauthenticated(tokenValue, targetEmail, ModifyToken.AuthenticationStrategy.EMAIL);
        delegatingTokenManager.authenticate(authRequest);
        memberCommand.modifyPassword(targetEmail, newPassword);
        delegatingTokenManager.remove(authRequest);
        return ApiResponse.ok();
    }

    @GetMapping("/join/space/code")
    public ApiResponse<CodeResponse> joinSpace(@AuthenticationPrincipal UserPrincipal userPrincipal, Long spaceId) {
        Long id = userPrincipal.id();
        // 이 유저가 코드 발행 권한이 있는지

        String verificationCode = verifyService.issueCode(VerifyService.Purpose.JOIN_SPACE, String.valueOf(spaceId));
        return ApiResponse.ok(new CodeResponse(verificationCode));
    }
}
