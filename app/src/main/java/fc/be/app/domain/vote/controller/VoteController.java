package fc.be.app.domain.vote.controller;

import fc.be.app.domain.vote.controller.dto.CandidateAddApiRequest;
import fc.be.app.domain.vote.controller.dto.VoteCreateApiRequest;
import fc.be.app.domain.vote.service.VoteManageUseCase;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.http.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static fc.be.app.domain.vote.service.VoteInfoQueryUseCase.VotesResponse;
import static fc.be.app.domain.vote.service.VoteManageUseCase.CandidateAddRequest;
import static fc.be.app.domain.vote.service.VoteManageUseCase.VoteCreateRequest;

@RequestMapping("/votes")
@RestController
public class VoteController {

    private final VoteManageUseCase voteManageUseCase;

    public VoteController(VoteManageUseCase voteManageUseCase) {
        this.voteManageUseCase = voteManageUseCase;
    }

    @PostMapping
    public ApiResponse<Long> createNewVote(@Valid VoteCreateApiRequest request,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.created(voteManageUseCase.createVote(
                new VoteCreateRequest(
                        userPrincipal.id(),
                        request.spaceId(),
                        request.title())));
    }

    @PostMapping("/{voteId}/candidates")
    public ApiResponse<List<VotesResponse>> findVotesInSpace(@PathVariable Long voteId,
                                                             CandidateAddApiRequest request,
                                                             @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.ok(voteManageUseCase.addCandidate(
                new CandidateAddRequest(
                        userPrincipal.id(),
                        voteId,
                        request.datas())));
    }


}
