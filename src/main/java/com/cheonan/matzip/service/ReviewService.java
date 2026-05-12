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

    @Transactional
    public void createService(ReviewRequest req) {
        //리뷰 본문 저장, 생성된 리뷰 번호 받아오기
        Long reviewId = reviewDao.insertReview(req);

        //사진이 있으면 리뷰번호와 연결해서 저장(반복문)
        if (req.getImageUrls() !=null) {
            for (String url : req.getImageUrls()) {
                reviewDao.insertReviewImage(reviewId, url);
            }
        }

        //태그(가성비, 혼밥 등)가 있으면 리뷰 번호와 연결해서 저장
        if (req.getSituations() !=null) {
            for (String situation : req.getSituations()) {
                reviewDao.insertReviewSituation(reviewId,situation);
            }
        }

        //식당의 리뷰(댓글) 카운트 즉시 갱신
        reviewDao.updateRestaurantReviewCount(req.getRestaurantId());
    }

    @Transactional
    public String toggleInteraction(Long reviewId, Long memberId, String type) {
        //이미 눌렀는지 확인
        boolean exist = reviewDao.checkInteractionExists(reviewId, memberId,type);

        //눌렀던 기록이 있으면 삭제(취소), 없다면 추가(등록) 후 결과 반환
        if (exist) {
            reviewDao.removeInteraction(reviewId, memberId, type);
            return "REMOVED";
        } else {reviewDao.addInteraction(reviewId, memberId, type);
            return "ADDED";
        }
    }
}
