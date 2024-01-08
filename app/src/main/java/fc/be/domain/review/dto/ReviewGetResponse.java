package fc.be.domain.review.dto;

import fc.be.domain.review.entity.Review;

import java.util.ArrayList;
import java.util.List;

public record ReviewGetResponse(List<Item> reviews) {

    public static ReviewGetResponse from(List<Review> reviews) {
        List<Item> items = new ArrayList<>();
        for(var review : reviews){
            items.add(
                    new Item(
                            review.getMember().getNickname(),
                            null, // todo [Review] Member Filed 추가되면 작업 필요
                            review.getRating(),
                            review.getContent(),
                            review.getIsGoogle()
                    )
            );
        }
        return new ReviewGetResponse(items);
    }

    private record Item(
            String nickname,
            String profile,
            Integer rating,
            String content,
            Boolean isGoogle
    ){

    }
}
