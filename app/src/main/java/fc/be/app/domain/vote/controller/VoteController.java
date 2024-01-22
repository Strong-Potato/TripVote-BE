package fc.be.app.domain.vote.controller;

import fc.be.app.domain.vote.controller.dto.request.CandidateAddApiRequest;
import fc.be.app.domain.vote.controller.dto.request.VoteCreateApiRequest;
import fc.be.app.domain.vote.service.dto.response.VoteDetailResponse;
import fc.be.app.domain.vote.service.dto.response.VoteResultResponse;
import fc.be.app.domain.vote.service.dto.response.VotesResponse;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.http.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static fc.be.app.domain.vote.repository.VoteRepositoryCustom.SearchCondition;

@Tag(name = "투표", description = "투표 API")
public interface VoteController {

    @Operation(
            operationId = "create Vote",
            summary = "투표를 생성하는 API",
            description = "투표를 생성하는 API입니다.",
            tags = {"vote"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                            }),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "NOT_FOUND")
            },
            security = {
                    @SecurityRequirement(name = "ACCESS_TOKEN", scopes = {"write:votes"})
            }
    )
    @PostMapping
    ApiResponse<String> createNewVote(
            @Valid VoteCreateApiRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @PostMapping("/{voteId}/candidates")
    ApiResponse<VoteDetailResponse> addCandidate(
            @PathVariable Long voteId,
            CandidateAddApiRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );


    @GetMapping
    ApiResponse<VotesResponse> findVotesInSpace(
            @ModelAttribute SearchCondition searchCondition,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @GetMapping("/{voteId}")
    ApiResponse<VoteDetailResponse> findVote(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @GetMapping("/{voteId}/result")
    ApiResponse<VoteResultResponse> findVoteResult(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @PutMapping("/{voteId}/voteStatus")
    ApiResponse<Void> changeVoteStatus(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @DeleteMapping("/{voteId}")
    ApiResponse<Void> deleteVote(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @GetMapping("/notVoted")
    ApiResponse<VotesResponse> notVotedList(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );
}
