package fc.be.app.domain.vote.controller;

import fc.be.app.domain.vote.controller.dto.request.*;
import fc.be.app.domain.vote.controller.dto.response.VoteUpdateApiResponse;
import fc.be.app.domain.vote.service.dto.request.CandidateAddRequest;
import fc.be.app.domain.vote.service.dto.request.CandidateDeleteRequest;
import fc.be.app.domain.vote.service.dto.request.VoteCreateRequest;
import fc.be.app.domain.vote.service.dto.request.VotingRequest;
import fc.be.app.domain.vote.service.dto.response.VoteDetailResponse;
import fc.be.app.domain.vote.service.dto.response.VoteResultResponse;
import fc.be.app.domain.vote.service.dto.response.VotesResponse;
import fc.be.app.domain.vote.service.service.VoteInfoQueryService;
import fc.be.app.domain.vote.service.service.VoteManageService;
import fc.be.app.domain.vote.service.service.VotingService;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.http.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static fc.be.app.domain.vote.repository.VoteRepositoryCustom.SearchCondition;

@RequestMapping("/votes")
@RestController
public class VoteControllerImpl implements VoteController {

    private static final String VOTE_API_PREFIX = "/votes/";

    private final VoteManageService voteManageService;
    private final VoteInfoQueryService voteInfoQueryService;
    private final VotingService votingService;

    public VoteControllerImpl(
            VoteManageService voteManageService,
            VoteInfoQueryService voteInfoQueryService, VotingService votingService
    ) {
        this.voteManageService = voteManageService;
        this.voteInfoQueryService = voteInfoQueryService;
        this.votingService = votingService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<String> createNewVote(
            @Valid @RequestBody VoteCreateApiRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ApiResponse.created(VOTE_API_PREFIX + voteManageService.createVote(
                new VoteCreateRequest(
                        userPrincipal.id(),
                        request.spaceId(),
                        request.title())));
    }

    @PostMapping("/{voteId}/candidates")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<VoteDetailResponse> addCandidate(
            @PathVariable Long voteId,
            @Valid @RequestBody CandidateAddApiRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ApiResponse.ok(voteManageService.addCandidate(
                new CandidateAddRequest(
                        userPrincipal.id(),
                        voteId,
                        request.candidateInfos())));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<VotesResponse> findVotesInSpace(
            @ModelAttribute SearchCondition searchCondition,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ApiResponse.ok(voteInfoQueryService.searchVotes(userPrincipal.id(), searchCondition));
    }

    @GetMapping("/{voteId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<VoteDetailResponse> findVote(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ApiResponse.ok(voteInfoQueryService.findByVoteId(voteId, userPrincipal.id()));
    }

    @GetMapping("/{voteId}/result")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<VoteResultResponse> findVoteResult(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ApiResponse.ok(voteInfoQueryService.findResultByVoteId(voteId, userPrincipal.id()));
    }

    @PutMapping("/{voteId}/voteStatus")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> changeVoteStatus(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        voteManageService.changeVoteStatus(voteId, userPrincipal.id());
        return ApiResponse.ok();
    }

    @DeleteMapping("/{voteId}/voteStatus")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> deleteVote(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        voteManageService.deleteVote(voteId, userPrincipal.id());
        return ApiResponse.ok();
    }

    @GetMapping("/notVoted")
    public ApiResponse<VotesResponse> notVotedList(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.ok(voteInfoQueryService.findMemberVotes(userPrincipal.id()));
    }

    @DeleteMapping("/{voteId}/candidates")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> deleteCandidates(
            @PathVariable Long voteId,
            @Valid CandidateDeleteApiRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        voteManageService.deleteCandidates(new CandidateDeleteRequest(voteId, userPrincipal.id(), request.candidateIds()));
        return ApiResponse.ok();
    }

    @PutMapping("/{voteId}/reset")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> resetVote(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        voteManageService.resetVote(voteId, userPrincipal.id());
        return ApiResponse.ok();
    }

    @PostMapping("/voting")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> voting(
            @RequestBody @Valid VotingApiRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        votingService.voteOrCancel(new VotingRequest(request.voteId(), userPrincipal.id(), request.candidateId()));
        return ApiResponse.ok();
    }

    @PatchMapping("/{voteId}")
    public ApiResponse<VoteUpdateApiResponse> updateVoteTitle(
            @PathVariable Long voteId,
            @RequestBody @Valid VoteUpdateApiRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ApiResponse.ok(voteManageService.updateVoteTitle(voteId, userPrincipal.id(), request));
    }

}
