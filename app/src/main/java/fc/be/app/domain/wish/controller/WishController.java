package fc.be.app.domain.wish.controller;

import fc.be.app.domain.wish.dto.WishAddRequest;
import fc.be.app.domain.wish.dto.WishAddResponse;
import fc.be.app.domain.wish.dto.WishGetResponse;
import fc.be.app.domain.wish.service.WishService;
import fc.be.app.global.http.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @GetMapping
    public ApiResponse<WishGetResponse> getWishList(
            @RequestParam(defaultValue = "R") char arrange,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<Character, String> sortedByMap = Map.of(
                'O', "place.title",
                'Q', "place.modifiedTime",
                'R', "createdDate"
        );

        Sort sort = Sort.by(sortedByMap.get(arrange));

        sort = switch (arrange) {
            case 'Q', 'R' -> sort.descending();
            default -> sort.ascending();
        };

        Pageable pageable = PageRequest.of(page, size, sort);

        return ApiResponse.ok(
                wishService.getWishes(1L, pageable)
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
