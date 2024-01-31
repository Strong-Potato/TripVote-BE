package fc.be.app.common.authentication.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import fc.be.app.common.authentication.controller.dto.request.*;
import fc.be.app.common.authentication.controller.dto.response.CodeResponse;
import fc.be.app.common.authentication.controller.dto.response.TokenResponse;
import fc.be.app.common.authentication.exception.AuthErrorCode;
import fc.be.app.common.authentication.exception.AuthException;
import fc.be.app.common.authentication.manager.DelegatingTokenManager;
import fc.be.app.common.authentication.model.JoinSpaceToken;
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
import fc.be.app.domain.member.service.MemberQuery.MemberResponse;
import fc.be.app.domain.space.service.SpaceService;
import fc.be.app.global.config.mail.service.MailService;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.http.ApiResponse;
import fc.be.app.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final VerifyService verifyService;
    private final DelegatingTokenManager delegatingTokenManager;
    private final MemberQuery memberQuery;
    private final MemberCommand memberCommand;
    private final MailService mailService;
    private final SpaceService spaceService;

    @PostMapping("/register/send-email")
    public ApiResponse<Void> sendCodeToEmail(@Valid @RequestBody SendEmailRequest request) {
        String targetEmail = request.email();
        MemberRequest memberRequest = new MemberRequest(targetEmail);
        boolean isExists = memberQuery.exists(memberRequest);
        if (isExists) {
            throw new MemberException(MemberErrorCode.EMAIL_ALREADY_EXISTS);
        }
        String verificationCode = verifyService.lockableIssue(VerifyService.Purpose.EMAIL, targetEmail);
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
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("isAuthenticated()")
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
        String verificationCode = verifyService.lockableIssue(VerifyService.Purpose.EMAIL, targetEmail);
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

    @PostMapping("/join/spaces/{spaceId}/code")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<CodeResponse> joinSpace(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long spaceId) {
        Long id = userPrincipal.id();
        MemberResponse memberResponse =
                memberQuery.findById(id).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        spaceService.getSpaceById(spaceId, id);
        String verificationCode = verifyService.lockableIssue(VerifyService.Purpose.JOIN_SPACE, String.valueOf(spaceId));
        Map<String, String> codeInfo = new HashMap<>();
        codeInfo.put("issuer", memberResponse.nickname());
        codeInfo.put("issuedAt", LocalDateTime.now().toString());
        codeInfo.put("expireAt", LocalDateTime.now().plus(VerifyService.Purpose.JOIN_SPACE.getCodeDuration()).toString());
        verifyService.setCodeInfo(VerifyService.Purpose.JOIN_SPACE, verificationCode, codeInfo);
        return ApiResponse.ok(new CodeResponse(verificationCode));
    }

    @GetMapping("/join/spaces/{spaceId}/token")
    public void joinSpaceToken(@PathVariable Long spaceId, @RequestParam String code, HttpServletResponse response) throws IOException {
        try {
            verifyService.verify(VerifyService.Purpose.JOIN_SPACE, String.valueOf(spaceId), code);
        } catch (AuthException exception) {
            Instant expiredInstant = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
            String expiredEmptyJwt = JWT.create().withExpiresAt(expiredInstant).sign(Algorithm.none());
            CookieUtil.addCookieNotHttpOnly(response, "join_space_token", expiredEmptyJwt, 60 * 5);
            response.sendRedirect("https://tripvote.site");
            return;
        }
        Map<String, String> codeInfo = verifyService.getCodeInfo(VerifyService.Purpose.JOIN_SPACE, code);
        JoinSpaceToken genRequest = JoinSpaceToken.unauthenticated(null, codeInfo.get("issuer"), spaceId);
        Token generatedToken = delegatingTokenManager.generate(genRequest);
        CookieUtil.addCookieNotHttpOnly(response, "join_space_token", generatedToken.getTokenValue(), 60 * 60 * 2);
        response.sendRedirect("https://tripvote.site");
    }

    @PostMapping("/join/spaces/{spaceId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> joinSpace(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long spaceId, @CookieValue(name = "join_space_token") String joinSpaceToken) {
        JoinSpaceToken authRequest = JoinSpaceToken.unauthenticated(joinSpaceToken, null, spaceId);
        delegatingTokenManager.authenticate(authRequest);
        spaceService.joinMember(spaceId, userPrincipal.id());
        return ApiResponse.ok();
    }
}
