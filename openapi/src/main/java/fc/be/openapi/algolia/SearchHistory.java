package fc.be.openapi.algolia;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
record SearchHistory(
        String objectID,
        String keyword,
        int hitCount,
        LocalDateTime modifiedAt
) {
    SearchHistory updateInfo() {
        return this.toBuilder()
                .hitCount(this.hitCount() + 1)
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}
