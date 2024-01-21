package fc.be.app.domain.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationType {

    /**
     * COMMON
     */
    AD("ad"),
    NOTICE("notice"),

    /**
     * REVIEW
     */
    REVIEW_REQUEST("reviewRequest"),

    /**
     * VOTE
     */
    VOTE_CREATED("voteCreated"),
    CANDIDATE_ADDED("candidateAdd"),
    VOTE_DONE("voteFinished"),
    VOTE_DELETED("voteDeleted"),

    /**
     * SPACE
     */
    MEMBER_INVITED("newMemberEnterSpace"),
    MEMBER_EXIT("newMemberExitSpace"),
    NEW_JOURNEY_ADDED("newJourneyAdded"),
    SPACE_SCHEDULE_CHANGED("newInfoUpdated"),
    SPACE_LOCATION_CHANGED("locationChanged")
    ;

    private final String type;
}
