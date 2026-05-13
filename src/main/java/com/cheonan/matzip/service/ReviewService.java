package com.cheonan.matzip.service;

import com.cheonan.matzip.dao.ReviewDao;
import com.cheonan.matzip.dto.request.ReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewDao reviewDao;
    private final MemberService memberService;  // MemberService 주입

    @Transactional
    public void createReview(ReviewRequest req, String email) {

        // memberId 조회 및 세팅 (Service 레이어에서 처리)
        Long memberId = memberService.getMemberIdByEmail(email);
        req.setMemberId(memberId);

        Long reviewId = reviewDao.insertReview(req);

        if (req.getImageUrls() != null) {
            for (String url : req.getImageUrls()) {
                reviewDao.insertReviewImage(reviewId, url);
            }
        }

        if (req.getSituations() != null) {
            for (String situation : req.getSituations()) {
                reviewDao.insertReviewSituation(reviewId, situation);
            }
        }

        reviewDao.updateRestaurantReviewCount(req.getRestaurantId());
    }

    @Transactional
    public String toggleInteraction(Long reviewId, String email, String type) {

        // memberId 조회 (Service 레이어에서 처리)
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
}