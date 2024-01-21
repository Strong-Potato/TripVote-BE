package fc.be.app.domain.vote.controller;

import fc.be.app.domain.vote.controller.dto.request.CandidateAddApiRequest;
import fc.be.app.domain.vote.controller.dto.request.CandidateDeleteApiRequest;
import fc.be.app.domain.vote.controller.dto.request.VoteCreateApiRequest;
import fc.be.app.domain.vote.service.dto.response.VoteDetailResponse;
import fc.be.app.domain.vote.service.dto.response.VoteResultResponse;
import fc.be.app.domain.vote.service.dto.response.VotesResponse;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.http.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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
            security = {
                    @SecurityRequirement(name = "ACCESS_TOKEN", scopes = {"write:votes"})
            }
    )
    @PostMapping
    ApiResponse<String> createNewVote(
            @Valid VoteCreateApiRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @Operation(
            operationId = "Adding Candidate",
            summary = "투표 후보를 추가하는 API",
            description = "투표 후보를 추가하는 API",
            security = {
                    @SecurityRequirement(name = "ACCESS_TOKEN", scopes = {"write:votes"})
            }
    )
    @PostMapping("/{voteId}/candidates")
    ApiResponse<VoteDetailResponse> addCandidate(
            @PathVariable Long voteId,
            CandidateAddApiRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @Operation(
            operationId = "finding Votes by space",
            summary = "스페이스 내의 투표 목록을 조회하는 API",
            description = "스페이스 내의 투표 목록을 조회하는 API",
            security = {
                    @SecurityRequirement(name = "ACCESS_TOKEN", scopes = {"write:votes"})
            }
    )
    @GetMapping
    ApiResponse<VotesResponse> findVotesInSpace(
            @ModelAttribute SearchCondition searchCondition,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @Operation(
            operationId = "Get Vote Detail",
            summary = "투표를 상세 조회 API",
            description = "투표 상세 조회 API",
            security = {
                    @SecurityRequirement(name = "ACCESS_TOKEN", scopes = {"write:votes"})
            }
    )
    @GetMapping("/{voteId}")
    ApiResponse<VoteDetailResponse> findVote(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @Operation(
            operationId = "show vote result",
            summary = "투표를 결과를 조회하는 API",
            description = "투표를 결과를 조회하는 API입니다. 후보들은 투표 수를 기준으로 정렬되어 제공됩니다.",
            security = {
                    @SecurityRequirement(name = "ACCESS_TOKEN", scopes = {"write:votes"})
            }
    )
    @GetMapping("/{voteId}/result")
    ApiResponse<VoteResultResponse> findVoteResult(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @Operation(
            operationId = "show vote change status",
            summary = "투표를 상태를 변경하는 API",
            description = "투표를 결과를 조회하는 API",
            security = {
                    @SecurityRequirement(name = "ACCESS_TOKEN", scopes = {"write:votes"})
            }
    )
    @PutMapping("/{voteId}/voteStatus")
    ApiResponse<Void> changeVoteStatus(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @Operation(
            operationId = "delete Vote",
            summary = "투표를 삭제하는 API",
            description = "투표를 삭제하는 API입니다.",
            security = {
                    @SecurityRequirement(name = "ACCESS_TOKEN", scopes = {"write:votes"})
            }
    )
    @DeleteMapping("/{voteId}")
    ApiResponse<Void> deleteVote(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @Operation(
            operationId = "delete candidates",
            summary = "투표 후보 삭제하는 API",
            description = "투표를 삭제하는 API입니다. 복수의 candidate id 값들을 제공함으로 여러 후보를 삭제할 수 있습니다.",
            security = {
                    @SecurityRequirement(name = "ACCESS_TOKEN", scopes = {"write:votes"})
            }
    )
    @DeleteMapping("/{voteId}/candidates")
    ApiResponse<Void> deleteCandidates(
            @PathVariable Long voteId,
            @Valid CandidateDeleteApiRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @Operation(
            operationId = "reset Vote",
            summary = "재투표 API",
            description = "재투표를 진행하는 API 입니다. 회원의 모든 투표 정보들과 결과 보기 상태를 초기화합니다..",
            security = {
                    @SecurityRequirement(name = "ACCESS_TOKEN", scopes = {"write:votes"})
            }
    )
    @PutMapping("/{voteId}/reset")
    ApiResponse<Void> resetVote(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    );
}
