package com.cheonan.matzip.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReceiptListResponse {
    private Long id;
    private String content;
    private int rating;
    private String verificationStatus;
    private String receiptUrl;
    private LocalDateTime createdAt;
    private String nickname;
    private String restaurantName;

    public static ReceiptListResponse from(
            Long id, String content, int rating,
            String verificationStatus, String receiptUrl,
            LocalDateTime createdAt, String nickname, String restaurantName) {

        ReceiptListResponse res = new ReceiptListResponse();
        res.setId(id);
        res.setContent(content);
        res.setRating(rating);
        res.setVerificationStatus(verificationStatus);
        res.setReceiptUrl(receiptUrl);
        res.setCreatedAt(createdAt);
        res.setNickname(nickname);
        res.setRestaurantName(restaurantName);
        return res;
    }
}