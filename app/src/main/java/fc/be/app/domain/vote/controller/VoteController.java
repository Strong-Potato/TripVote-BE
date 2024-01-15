package fc.be.app.domain.vote.controller;

import fc.be.app.domain.vote.controller.dto.VoteCreateApiRequest;
import fc.be.app.domain.vote.service.VoteManageUseCase;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.http.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static fc.be.app.domain.vote.service.VoteManageUseCase.*;

@RequestMapping("/votes")
@RestController
public class VoteController {

    private final VoteManageUseCase voteCreateUseCase;

    public VoteController(VoteManageUseCase voteCreateUseCase) {
        this.voteCreateUseCase = voteCreateUseCase;
    }

    @PostMapping
    public ApiResponse<Long> createNewVote(@Valid VoteCreateApiRequest request,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.created(voteCreateUseCase.createVote(
                new VoteCreateRequest(
                        userPrincipal.id(),
                        request.spaceId(),
                        request.title())));
    }
}
