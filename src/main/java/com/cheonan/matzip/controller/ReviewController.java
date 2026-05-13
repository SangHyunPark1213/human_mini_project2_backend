package com.cheonan.matzip.controller;

import com.cheonan.matzip.dto.request.ReviewRequest;
import com.cheonan.matzip.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // ── 리뷰 등록 ───────────────────────────────────────
    @PostMapping
    public ResponseEntity<String> createReview(
            @RequestBody ReviewRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        // Controller는 email만 넘김 → 비즈니스 로직은 Service에서
        reviewService.createReview(req, userDetails.getUsername());
        return ResponseEntity.ok("등록 완료");
    }

    // ── 좋아요/도움돼요 토글 ────────────────────────────
    @PostMapping("/{reviewId}/interaction")
    public ResponseEntity<Map<String, String>> toggleInteraction(
            @PathVariable Long reviewId,
            @RequestParam String type,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        }

        // Controller는 email만 넘김
        String result = reviewService.toggleInteraction(
                reviewId, userDetails.getUsername(), type
        );
        return ResponseEntity.ok(Map.of("action", result));
    }
}