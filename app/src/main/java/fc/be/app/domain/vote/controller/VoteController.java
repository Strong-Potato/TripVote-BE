package fc.be.app.domain.vote.controller;

import fc.be.app.domain.vote.controller.dto.request.CandidateAddApiRequest;
import fc.be.app.domain.vote.controller.dto.request.VoteCreateApiRequest;
import fc.be.app.domain.vote.service.dto.request.CandidateAddRequest;
import fc.be.app.domain.vote.service.dto.request.VoteCreateRequest;
import fc.be.app.domain.vote.service.dto.response.VoteDetailResponse;
import fc.be.app.domain.vote.service.dto.response.VoteResultResponse;
import fc.be.app.domain.vote.service.dto.response.VotesResponse;
import fc.be.app.domain.vote.service.service.VoteInfoQueryService;
import fc.be.app.domain.vote.service.service.VoteManageService;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.http.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static fc.be.app.domain.vote.repository.VoteRepositoryCustom.SearchCondition;

@RequestMapping("/votes")
@RestController
public class VoteController {

    private final VoteManageService voteManageService;
    private final VoteInfoQueryService voteInfoQueryService;
    public VoteController(
            VoteManageService voteManageService,
            VoteInfoQueryService voteInfoQueryService
    ) {
        this.voteManageService = voteManageService;
        this.voteInfoQueryService = voteInfoQueryService;
    }

    @PostMapping
    public ApiResponse<Long> createNewVote(
            @Valid VoteCreateApiRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ApiResponse.created(voteManageService.createVote(
                new VoteCreateRequest(
                        userPrincipal.id(),
                        request.spaceId(),
                        request.title())));
    }

    @PostMapping("/{voteId}/candidates")
    public ApiResponse<VoteDetailResponse> addCandidate(
            @PathVariable Long voteId,
            CandidateAddApiRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ApiResponse.ok(voteManageService.addCandidate(
                new CandidateAddRequest(
                        userPrincipal.id(),
                        voteId,
                        request.candidateInfos())));
    }


    @GetMapping
    public ApiResponse<VotesResponse> findVotesInSpace(
            @ModelAttribute SearchCondition searchCondition,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ApiResponse.ok(voteInfoQueryService.searchVotes(userPrincipal.id(), searchCondition));
    }

    @GetMapping("/{voteId}")
    public ApiResponse<VoteDetailResponse> findVote(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ApiResponse.ok(voteInfoQueryService.findByVoteId(voteId, userPrincipal.id()));
    }

    @GetMapping("/{voteId}/result")
    public ApiResponse<VoteResultResponse> findVoteResult(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ApiResponse.ok(voteInfoQueryService.findResultByVoteId(voteId, userPrincipal.id()));
    }
}
