package fc.be.app.domain.space.controller;

import fc.be.app.domain.space.dto.request.UpdateSpaceRequest;
import fc.be.app.domain.space.dto.response.SpaceResponse;
import fc.be.app.domain.space.service.SpaceService;
import fc.be.app.domain.space.vo.SpaceType;
import fc.be.app.global.http.ApiResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/spaces")
public class SpaceController {

    private final SpaceService spaceService;

    @PostMapping
    public ApiResponse<SpaceResponse> createSpace() {
        //todo 로그인 구현 시 @authenticationprincipal 어노테이션 등으로 변경할 예정
        Long memberId = 1L;
        return ApiResponse.created(spaceService.createSpace(memberId));
    }

    @GetMapping("/{spaceId}")
    public ApiResponse<SpaceResponse> getSpaceById(@PathVariable Long spaceId) {
        return ApiResponse.ok(spaceService.getSpaceById(spaceId));
    }

    @PutMapping("/{spaceId}/title")
    public ApiResponse<SpaceResponse> updateSpaceByTitle(@PathVariable Long spaceId,
        @Valid @RequestBody UpdateSpaceRequest.TitleUpdateRequest updateRequest) {
        SpaceResponse spaceResponse = spaceService.updateSpaceByTitle(spaceId, updateRequest);
        return ApiResponse.ok(spaceResponse);
    }

    @PutMapping("/{spaceId}/dates")
    public ApiResponse<SpaceResponse> updateSpaceByDates(@PathVariable Long spaceId,
        @Valid @RequestBody UpdateSpaceRequest.DateUpdateRequest updateRequest) {
        SpaceResponse spaceResponse = spaceService.updateSpaceByDates(spaceId, updateRequest);
        return ApiResponse.ok(spaceResponse);
    }

    @GetMapping
    public ApiResponse<List<SpaceResponse>> getSpaceListWithType(@RequestParam SpaceType type) {
        // todo 로그인 구현 시 @AuthenticationPrincipal 어노테이션 등으로 변경할 예정
        Long memberId = 1L;
        List<SpaceResponse> spaceResponses = spaceService.findByEndDateAndMember(LocalDate.now(),
            memberId, type);
        return ApiResponse.ok(spaceResponses);
    }
}
