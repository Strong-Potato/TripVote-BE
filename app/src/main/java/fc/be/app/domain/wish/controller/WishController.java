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
    public ApiResponse<WishAddResponse> addWish(@Valid @RequestBody WishAddRequest wishAddRequest) {
        return ApiResponse.ok(
                wishService.addWish(wishAddRequest)
        );
    }

    @GetMapping("/{placeId}")
    public ApiResponse<Boolean> isWished(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Integer placeId
    ) {
        Long memberId = userPrincipal.id();
        return ApiResponse.ok(
                wishService.isWished(memberId, placeId)
        );
    }

    @DeleteMapping("{placeId}")
    public ApiResponse<Boolean> deleteWish(@PathVariable Integer placeId) {
        Long memberId = 1L;
        return ApiResponse.ok(
                wishService.deleteWish(memberId, placeId)
        );
    }
}
