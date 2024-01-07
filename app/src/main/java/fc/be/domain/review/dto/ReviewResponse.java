package fc.be.domain.review.dto;

import fc.be.domain.review.entity.Review;

import java.util.ArrayList;
import java.util.List;

public record ReviewResponse(List<Item> reviews) {

    public static ReviewResponse from(List<Review> reviews) {
        List<Item> items = new ArrayList<>();
        for(var review : reviews){
            items.add(
                    new Item(
                            review.getMember().getNickname(),
                            null, //review.getMember().getProfile(),
                            review.getRating(),
                            review.getContent(),
                            review.getIsGoogle()
                    )
            );
        }
        return new ReviewResponse(items);
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
