package com.cheonan.matzip.service;

import com.cheonan.matzip.dao.ReviewDao;
import com.cheonan.matzip.dto.Review;
import com.cheonan.matzip.dto.request.ReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewDao reviewDao;

    //댓글 생성
    @Transactional
    public void createReview(ReviewRequest req) {
        //리뷰 본문 저장, 생성된 리뷰 번호 받아오기
        Long reviewId = reviewDao.insertReview(req);

        //음식 사진 저장 (최대 3장)
        if (req.getFoodImageUrls() != null) {
            //최대 3장까지만 저장하도록 제한 (방어 코드)
            int limit = Math.min(req.getFoodImageUrls().size(), 3);
            for (int i = 0; i < limit; i++) {
                String url = req.getFoodImageUrls().get(i);
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

    //댓글 수정
    //디비내에서는 수정된게 저장되면 태그와 사진이 전부 삭제된 뒤, 기존과 동일한 데이터를 다시 등록
    @Transactional
    public void updateReview(Long reviewId, Long currentMemberId, ReviewRequest req) {

        //DB에서 작성자 ID를 가져와서 지금 유저(currentMemberId)와 비교
        Long writerId = reviewDao.getReviewWriterId(reviewId);
        if (!writerId.equals(currentMemberId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        //기본 내용 수정 및 기존 사진/태그 삭제
        reviewDao.updateReview(req, reviewId);

        //디비청소 (DELETE)
        reviewDao.deleteReviewImages(reviewId);
        reviewDao.deleteReviewSituations(reviewId);

        //새로운 음식 사진 저장 (최대 3장), 수정 안할거면 유지
        if (req.getFoodImageUrls() != null) {
            int limit = Math.min(req.getFoodImageUrls().size(), 3);
            for (int i = 0; i < limit; i++) {
                String url = req.getFoodImageUrls().get(i);
                reviewDao.insertReviewImage(reviewId, url);
            }
        }

        //새로운 태그 저장, 수정 안할거면 유지
        if (req.getSituations() != null) {
            for (String situation : req.getSituations()) {
                reviewDao.insertReviewSituation(reviewId, situation);
            }
        }

        //평점 변동이 있을 수 있으니 식당 통계 갱신
        reviewDao.updateRestaurantReviewCount(req.getRestaurantId());
    }


    //댓글 삭제
    @Transactional
    public void deleteReview(Long reviewId, Long currentMemberId, Long restaurantId) {
        //DB에서 작성자 ID를 가져와서 지금 유저(currentMemberId)와 비교
        Long writerId = reviewDao.getReviewWriterId(reviewId);
        if (!writerId.equals(currentMemberId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        //리뷰 본문 삭제, 이때 DB의 CASCADE 설정 덕분에 사진과 태그도 함께 삭제됨
        reviewDao.deleteReview(reviewId);
        //리뷰가 사라졌으니 식당의 리뷰 개수와 평균 평점을 즉시 갱신
        reviewDao.updateRestaurantReviewCount(restaurantId);
    }


    //좋아요 도움되요
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
