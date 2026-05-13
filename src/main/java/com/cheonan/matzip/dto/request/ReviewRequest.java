package com.cheonan.matzip.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewRequest {
    private Long restaurantId;
    private Long memberId;
    private int rating;
    private String content;
    private String revisit;
    private String receiptUrl;
    private List<String> imageUrls;      // ✅ foodImageUrls → imageUrls 통일
    private List<String> situations;
}

@Getter
@Setter
class ReviewInteractionRequest {
    private Long reviewId;
    // ✅ memberId 제거
    private String interactionType;
}