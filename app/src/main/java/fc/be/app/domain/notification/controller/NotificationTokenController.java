package fc.be.app.domain.notification.controller;

import fc.be.app.domain.notification.application.NotificationTokenRegisterService;
import fc.be.app.domain.notification.application.dto.request.TokenRegisterRequest;
import fc.be.app.domain.notification.controller.dto.TokenRegisterApiRequest;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.http.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationTokenController {

    private final NotificationTokenRegisterService notificationTokenRegisterService;

    @PostMapping("/notifications/token")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> createFcmToken(
            @Valid @RequestBody TokenRegisterApiRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        notificationTokenRegisterService.registerFcmToken(new TokenRegisterRequest(request.token(), userPrincipal.id()));
        return ApiResponse.ok();
    }
}
