package com.cheonan.matzip.dao;

import com.cheonan.matzip.dto.request.ReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewDao {

    private final JdbcTemplate jdbcTemplate;

    public Long insertReview(ReviewRequest req) {

        // ① NEXTVAL 먼저 채번
        Long reviewId = jdbcTemplate.queryForObject(
                "SELECT REVIEW_SEQ.NEXTVAL FROM DUAL", Long.class
        );

        // ② 채번된 ID로 INSERT
        String sql = "INSERT INTO REVIEW(id, restaurant_id, member_id, rating, content, revisit, receipt_url) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                reviewId,
                req.getRestaurantId(),
                req.getMemberId(),
                req.getRating(),
                req.getContent(),
                req.getRevisit(),
                req.getReceiptUrl()
        );

        return reviewId;  // ③ 채번된 ID 반환
    }

    //리뷰 이미지 저장 (최대 3개)
    public void insertReviewImage(Long reviewId, String imageUrl) {
        String sql = "INSERT INTO REVIEW_IMAGE(id, review_id, image_url)" +
                "VALUES (REVIEW_IMAGE_SEQ.NEXTVAL, ?, ?)";
        jdbcTemplate.update(sql, reviewId, imageUrl);
    }

    //태그 저장 (데이트, 가성비 등)
    public void insertReviewSituation(Long reviewId, String situation) {
        String sql = "INSERT INTO REVIEW_SITUATION(review_id, situation) VALUES (?, ?)";
        jdbcTemplate.update(sql, reviewId, situation);
    }

    public void updateRestaurantReviewCount(Long restaurantId) {
        String sql = "UPDATE RESTAURANT " +
                "SET review_count = (SELECT COUNT(*) FROM REVIEW WHERE restaurant_id = ?) "
                + "WHERE id = ?";

        //첫 번째 ?에는 개수를 셀 식당 ID, 두 번째 ?에는 업데이트할 식당 ID
        jdbcTemplate.update(sql, restaurantId, restaurantId);
    }

    //좋아요와 도움되요 버튼 처리 코드
    //사용자가 버튼을 눌렀는지 확인
    public boolean checkInteractionExists(Long reviewId, Long memberId, String type) {
        //버튼종류에 따라 보는 테이블 결정
        String tableName = type.equals("LIKE") ? "REVIEW_LIKE" : "REVIEW_HELPFUL";
        //멤버가 좋아요나 도움되요 버튼을 누른 횟수 세기
        String sql = "SELECT COUNT (*) FROM " + tableName + " WHERE review_id = ? AND member_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, reviewId, memberId);
        //개수가 0보다 크면 누른상태(true)고 0이면 안누른상태(false)
        return count != null && count > 0;
    }


    //버튼 클릭 시 기록 추가 및 카운트 +1
    public void addInteraction(Long reviewId, Long memberId, String type) {
        //버튼 눌렀을 때 카운트를 올리고 기록함
        String tableName = type.equals("LIKE") ? "REVIEW_LIKE" : "REVIEW_HELPFUL";
        String updateCol = type.equals("LIKE") ? "like_count" : "helpful_count";

        //누가 어떤 버튼을 남겼는지 기록추가
        jdbcTemplate.update("INSERT INTO " + tableName + "(review_id, member_id) VALUES(?, ?)"
                , reviewId, memberId);

        //리뷰 테이블의 해당 리뷰의 카운트 숫자 +1
        jdbcTemplate.update("UPDATE REVIEW SET " + updateCol
                + "=" + updateCol + " + 1 WHERE id = ?", reviewId);
    }


    //버튼 클릭 시 기록 삭제 및 카운트 -1
    public void removeInteraction(Long reviewId, Long memberId, String type) {
        String tableName = type.equals("LIKE") ? "REVIEW_LIKE" : "REVIEW_HELPFUL";
        String updateCol = type.equals("LIKE") ? "like_count" : "helpful_count";

        //해당 인터랙션 테이블에서 기존 클릭 기록 삭제
        jdbcTemplate.update("DELETE FROM " + tableName + " WHERE review_id = ? AND member_id = ?", reviewId, memberId);

        //리뷰 테이블의 해당 리뷰의 카운트 숫자 -1
        jdbcTemplate.update("UPDATE REVIEW SET " + updateCol + " = " + updateCol + " - 1 WHERE id = ?", reviewId);
    }
}
