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

import java.util.Optional;

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
        Long memberId = Optional.of(userPrincipal.id()).orElse(0L);
        return ApiResponse.ok(
                wishService.addWish(memberId, wishAddRequest)
        );
    }

    @GetMapping("/{placeId}")
    public ApiResponse<Boolean> isWished(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Integer placeId
    ) {
        Long memberId = Optional.of(userPrincipal.id()).orElse(0L);
        return ApiResponse.ok(
                wishService.isWished(memberId, placeId)
        );
    }

    @DeleteMapping("{placeId}")
    public ApiResponse<Boolean> deleteWish(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Integer placeId
    ) {
        Long memberId = Optional.of(userPrincipal.id()).orElse(0L);
        return ApiResponse.ok(
                wishService.deleteWish(memberId, placeId)
        );
    }
}
