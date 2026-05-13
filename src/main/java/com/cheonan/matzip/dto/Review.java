package com.cheonan.matzip.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class Review {
    private Long id;
    private Long restaurantId;
    private Long memberId;
    private int rating;
    private String content;
    private String revisit;
    private LocalDateTime createdAt;        // ✅ createAt → createdAt 오타 수정
    private String verificationStatus;
    private String receiptUrl;
    private int helpfulCount;
    private int likeCount;
    private List<String> imageUrls;         // ✅ foodImageUrls → imageUrls 통일
}