package fc.be.app.domain.wish.controller;

import fc.be.app.domain.wish.dto.WishAddRequest;
import fc.be.app.domain.wish.dto.WishAddResponse;
import fc.be.app.domain.wish.service.WishService;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.http.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishes")
public class WishController {

    private final WishService wishService;

    @PostMapping
    public ApiResponse<WishAddResponse> addWish(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody WishAddRequest wishAddRequest
    ) {
        return ApiResponse.ok(
                wishService.addWish(userPrincipal.id(), wishAddRequest)
        );
    }

    @GetMapping("/{placeId}")
    public ApiResponse<Boolean> isWished(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Integer placeId
    ) {
        return ApiResponse.ok(
                wishService.isWished(userPrincipal.id(), placeId)
        );
    }

    @DeleteMapping("{placeId}")
    public ApiResponse<Boolean> deleteWish(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Integer placeId
    ) {
        return ApiResponse.ok(
                wishService.deleteWish(userPrincipal.id(), placeId)
        );
    }
}
