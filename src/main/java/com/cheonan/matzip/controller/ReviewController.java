package com.cheonan.matzip.controller;

import com.cheonan.matzip.dto.request.ReviewRequest;
import com.cheonan.matzip.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    //리뷰 등록
    @PostMapping
    public ResponseEntity<String> createReview(@RequestBody ReviewRequest req) {
        reviewService.createReview(req);
        return ResponseEntity.ok("등록 완료");
    }

    //리뷰 상호작용 like
    @PostMapping("/{reviewId}/interaction")
    public ResponseEntity<Map<String, String >>toggleInteraction(
            @PathVariable Long reviewId,
            @RequestParam Long memberId,
            @RequestParam String type) {

                String result = reviewService.toggleInteraction(reviewId, memberId, type);
                return ResponseEntity.ok(Map.of("action", result));
    }

    //리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(
            @PathVariable Long reviewId,
            @RequestParam Long memberId, //보통 세션이나 토큰에서 가져오지만 지금은 파라미터로 처리
            @RequestBody ReviewRequest req) {

        reviewService.updateReview(reviewId, memberId, req);
        return ResponseEntity.ok("수정 완료");
    }

    //리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long reviewId,
            @RequestParam Long memberId,
            @RequestParam Long restaurantId) {

        reviewService.deleteReview(reviewId, memberId, restaurantId);
        return ResponseEntity.ok("삭제 완료");
    }
}