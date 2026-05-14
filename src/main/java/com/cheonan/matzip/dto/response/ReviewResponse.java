package com.cheonan.matzip.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReviewResponse {
    private Long id;
    private int rating;
    private String content;
    private LocalDateTime createdAt;
    private String nickname;
    private List<String> imageUrls;
    private List<String> situations;
    private int helpfulCount;
    private String verificationStatus;

    public static ReviewResponse of(
            Long id, int rating, String content,
            LocalDateTime createdAt, String nickname,
            int helpfulCount, String verificationStatus) {

        ReviewResponse res = new ReviewResponse();
        res.setId(id);
        res.setRating(rating);
        res.setContent(content);
        res.setCreatedAt(createdAt);
        res.setNickname(nickname);
        res.setHelpfulCount(helpfulCount);
        res.setVerificationStatus(verificationStatus);
        return res;
    }
}