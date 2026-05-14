package com.cheonan.matzip.service;

import com.cheonan.matzip.dao.ReviewDao;
import com.cheonan.matzip.dto.request.ReviewRequest;
import com.cheonan.matzip.dto.response.ReceiptListResponse;
import com.cheonan.matzip.dto.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewDao reviewDao;
    private final MemberService memberService;

    // ── 리뷰 등록 ────────────────────────────────────────
    @Transactional
    public void createReview(ReviewRequest req, String email) {

        Long memberId = memberService.getMemberIdByEmail(email);
        req.setMemberId(memberId);

        Long reviewId = reviewDao.insertReview(req);

        // 이미지 저장 (최대 3장)
        if (req.getImageUrls() != null) {
            int limit = Math.min(req.getImageUrls().size(), 3);
            for (int i = 0; i < limit; i++) {
                reviewDao.insertReviewImage(reviewId, req.getImageUrls().get(i));
            }
        }

        // 태그 저장
        if (req.getSituations() != null) {
            for (String situation : req.getSituations()) {
                reviewDao.insertReviewSituation(reviewId, situation);
            }
        }

        reviewDao.updateRestaurantReviewCount(req.getRestaurantId());
    }

    // ── 리뷰 수정 ────────────────────────────────────────
    @Transactional
    public void updateReview(Long reviewId, ReviewRequest req, String email) {

        // 작성자 본인 확인
        Long memberId = memberService.getMemberIdByEmail(email);
        Long writerId = reviewDao.getReviewWriterId(reviewId);
        if (!writerId.equals(memberId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        // 기본 정보 수정
        reviewDao.updateReview(req, reviewId);

        // 이미지/태그 재등록 (DELETE → INSERT)
        reviewDao.deleteReviewImages(reviewId);
        reviewDao.deleteReviewSituations(reviewId);

        if (req.getImageUrls() != null) {
            int limit = Math.min(req.getImageUrls().size(), 3);
            for (int i = 0; i < limit; i++) {
                reviewDao.insertReviewImage(reviewId, req.getImageUrls().get(i));
            }
        }

        if (req.getSituations() != null) {
            for (String situation : req.getSituations()) {
                reviewDao.insertReviewSituation(reviewId, situation);
            }
        }

        reviewDao.updateRestaurantReviewCount(req.getRestaurantId());
    }

    // ── 리뷰 삭제 ────────────────────────────────────────
    @Transactional
    public void deleteReview(Long reviewId, Long restaurantId, String email) {

        // 작성자 본인 확인
        Long memberId = memberService.getMemberIdByEmail(email);
        Long writerId = reviewDao.getReviewWriterId(reviewId);
        if (!writerId.equals(memberId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        // CASCADE로 이미지/태그 자동 삭제
        reviewDao.deleteReview(reviewId);
        reviewDao.updateRestaurantReviewCount(restaurantId);
    }

    // ── 좋아요/도움돼요 토글 ─────────────────────────────
    @Transactional
    public String toggleInteraction(Long reviewId, String email, String type) {

        Long memberId = memberService.getMemberIdByEmail(email);
        boolean exist = reviewDao.checkInteractionExists(reviewId, memberId, type);

        if (exist) {
            reviewDao.removeInteraction(reviewId, memberId, type);
            return "REMOVED";
        } else {
            reviewDao.addInteraction(reviewId, memberId, type);
            return "ADDED";
        }
    }

    // ── 영수증 목록 조회 ──────────────────────────────
    public List<ReceiptListResponse> getReceiptList() {
        return reviewDao.findAllWithReceipt();
    }

    // ── 영수증 승인 ───────────────────────────────────
    public void approveReceipt(Long reviewId) {
        reviewDao.updateVerificationStatus(reviewId, "A");
    }

    // ── 영수증 거절 ───────────────────────────────────
    public void rejectReceipt(Long reviewId) {
        reviewDao.updateVerificationStatus(reviewId, "R");
    }

    // ── 맛집 리뷰 목록 조회 ──────────────────────────────
    public List<ReviewResponse> getReviewsByRestaurantId(Long restaurantId) {

        List<ReviewResponse> reviews =
                reviewDao.findByRestaurantId(restaurantId);

        // 각 리뷰에 이미지, 태그 세팅
        for (ReviewResponse review : reviews) {
            review.setImageUrls(
                    reviewDao.findImageUrlsByReviewId(review.getId())
            );
            review.setSituations(
                    reviewDao.findSituationsByReviewId(review.getId())
            );
        }

        return reviews;
    }

}