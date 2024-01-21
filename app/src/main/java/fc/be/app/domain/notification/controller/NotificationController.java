package fc.be.app.domain.notification.controller;

import fc.be.app.domain.notification.application.NotificationCommandService;
import fc.be.app.domain.notification.application.NotificationQueryService;
import fc.be.app.domain.notification.application.NotificationSubscribeService;
import fc.be.app.domain.notification.application.dto.request.SubscribeRequest;
import fc.be.app.domain.notification.application.dto.response.NotificationsResponse;
import fc.be.app.domain.notification.controller.dto.SubScribeApiRequest;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.http.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/notifications")
@RestController
public class NotificationController {

    private final NotificationQueryService notificationQueryService;
    private final NotificationCommandService notificationCommandService;
    private final NotificationSubscribeService notificationSubscribeService;

    public NotificationController(
            NotificationQueryService notificationQueryService,
            NotificationCommandService notificationCommandService,
            NotificationSubscribeService notificationSubscribeService
    ) {
        this.notificationQueryService = notificationQueryService;
        this.notificationCommandService = notificationCommandService;
        this.notificationSubscribeService = notificationSubscribeService;
    }

    @GetMapping
    public ApiResponse<NotificationsResponse> getAllMemberNotifications(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ApiResponse.ok(notificationQueryService.findAllByMemberId(userPrincipal.id()));
    }

    @PostMapping("/subscribe")
    public ApiResponse<Void> subscribe(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody SubScribeApiRequest subScribeApiRequest
    ) {
        notificationSubscribeService.subscribe(
                new SubscribeRequest(
                        subScribeApiRequest.isGlobal(),
                        subScribeApiRequest.spaceId(),
                        userPrincipal.id()));
        return ApiResponse.ok();
    }

    @PatchMapping("/{notification-id}/read")
    public ApiResponse<Void> read(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("notification-id") final Long notificationId
    ) {
        notificationCommandService.read(userPrincipal.id(), notificationId);
        return ApiResponse.ok();
    }
}
