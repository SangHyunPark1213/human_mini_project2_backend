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

    @PostMapping
    public ResponseEntity<String> createReview(@RequestBody ReviewRequest req) {
        reviewService.createService(req);
        return ResponseEntity.ok("등록 완료");
    }

    @PostMapping("/{reviewId}/interaction")
    public ResponseEntity<Map<String, String >>toggleInteraction(
            @PathVariable Long reviewId,
            @RequestParam Long memberId,
            @RequestParam String type) {

                String result = reviewService.toggleInteraction(reviewId, memberId, type);
                return ResponseEntity.ok(Map.of("action", result));
    }
}
