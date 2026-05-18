package com.cheonan.matzip.dao;

import com.cheonan.matzip.dto.request.ReviewRequest;
import com.cheonan.matzip.dto.response.ReceiptListResponse;
import com.cheonan.matzip.dto.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewDao {

    private final JdbcTemplate jdbcTemplate;

    // ── 리뷰 ID로 작성자 조회 ────────────────────────────
    public Long getReviewWriterId(Long reviewId) {
        String sql = "SELECT member_id FROM REVIEW WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, reviewId);
    }

    // ── 리뷰 등록 ────────────────────────────────────────
    public Long insertReview(ReviewRequest req) {

        Long reviewId = jdbcTemplate.queryForObject(
                "SELECT REVIEW_SEQ.NEXTVAL FROM DUAL", Long.class
        );

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

        return reviewId;
    }

    // ── 리뷰 수정 ────────────────────────────────────────
    public void updateReview(ReviewRequest req, Long reviewId) {
        String sql = "UPDATE REVIEW SET rating = ?, content = ?, revisit = ?, "
                + "receipt_url = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                req.getRating(),
                req.getContent(),
                req.getRevisit(),
                req.getReceiptUrl(),
                reviewId
        );
    }

    // ── 리뷰 삭제 ────────────────────────────────────────
    public void deleteReview(Long reviewId) {
        jdbcTemplate.update("DELETE FROM REVIEW WHERE id = ?", reviewId);
    }

    // ── 이미지 등록 ──────────────────────────────────────
    public void insertReviewImage(Long reviewId, String imageUrl) {
        String sql = "INSERT INTO REVIEW_IMAGE(id, review_id, image_url) "
                + "VALUES (REVIEW_IMAGE_SEQ.NEXTVAL, ?, ?)";
        jdbcTemplate.update(sql, reviewId, imageUrl);
    }

    // ── 이미지 전체 삭제 (수정 시 재등록용) ──────────────
    public void deleteReviewImages(Long reviewId) {
        jdbcTemplate.update(
                "DELETE FROM REVIEW_IMAGE WHERE review_id = ?", reviewId
        );
    }

    // ── 태그 등록 ────────────────────────────────────────
    public void insertReviewSituation(Long reviewId, String situation) {
        String sql = "INSERT INTO REVIEW_SITUATION(review_id, situation) VALUES (?, ?)";
        jdbcTemplate.update(sql, reviewId, situation);
    }

    // ── 태그 전체 삭제 (수정 시 재등록용) ────────────────
    public void deleteReviewSituations(Long reviewId) {
        jdbcTemplate.update(
                "DELETE FROM REVIEW_SITUATION WHERE review_id = ?", reviewId
        );
    }

    // ── 식당 통계 갱신 (리뷰수 + 평균평점) ───────────────
    public void updateRestaurantReviewCount(Long restaurantId) {
        String sql = "UPDATE RESTAURANT "
                + "SET review_count   = (SELECT COUNT(*) FROM REVIEW WHERE restaurant_id = ?), "
                + "    average_rating = (SELECT NVL(AVG(rating), 0) FROM REVIEW WHERE restaurant_id = ?) "
                + "WHERE id = ?";
        jdbcTemplate.update(sql, restaurantId, restaurantId, restaurantId);
    }

    // ── 좋아요/도움돼요 존재 여부 확인 ───────────────────
    public boolean checkInteractionExists(Long reviewId, Long memberId, String type) {
        String tableName = type.equals("LIKE") ? "REVIEW_LIKE" : "REVIEW_HELPFUL";
        String sql = "SELECT COUNT(*) FROM " + tableName
                + " WHERE review_id = ? AND member_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, reviewId, memberId);
        return count != null && count > 0;
    }

    // ── 좋아요/도움돼요 추가 ─────────────────────────────
    public void addInteraction(Long reviewId, Long memberId, String type) {
        String tableName = type.equals("LIKE") ? "REVIEW_LIKE" : "REVIEW_HELPFUL";
        String updateCol = type.equals("LIKE") ? "like_count" : "helpful_count";

        jdbcTemplate.update(
                "INSERT INTO " + tableName + "(review_id, member_id) VALUES(?, ?)",
                reviewId, memberId
        );

        jdbcTemplate.update(
                "UPDATE REVIEW SET " + updateCol + " = " + updateCol + " + 1 WHERE id = ?",
                reviewId
        );
    }

    // ── 좋아요/도움돼요 취소 ─────────────────────────────
    public void removeInteraction(Long reviewId, Long memberId, String type) {
        String tableName = type.equals("LIKE") ? "REVIEW_LIKE" : "REVIEW_HELPFUL";
        String updateCol = type.equals("LIKE") ? "like_count" : "helpful_count";

        jdbcTemplate.update(
                "DELETE FROM " + tableName + " WHERE review_id = ? AND member_id = ?",
                reviewId, memberId
        );

        jdbcTemplate.update(
                "UPDATE REVIEW SET " + updateCol + " = " + updateCol + " - 1 WHERE id = ?",
                reviewId
        );
    }

    // ── 승인 대기 중인 영수증 목록 조회 ───────────────────
    public List<ReceiptListResponse> findAllWithReceipt() {
        String sql = """
                SELECT r.id, r.content, r.rating,
                       r.verification_status, r.receipt_url,
                       r.created_at,
                       m.nickname,
                       rs.name AS restaurant_name
                FROM REVIEW r
                JOIN MEMBER m ON r.member_id = m.id
                JOIN RESTAURANT rs ON r.restaurant_id = rs.id
                WHERE r.receipt_url IS NOT NULL
                AND r.verification_status = 'N'
                ORDER BY r.created_at DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                ReceiptListResponse.from(
                        rs.getLong("id"),
                        rs.getString("content"),
                        rs.getInt("rating"),
                        rs.getString("verification_status"),
                        rs.getString("receipt_url"),
                        rs.getTimestamp("created_at") != null
                                ? rs.getTimestamp("created_at").toLocalDateTime()
                                : null,
                        rs.getString("nickname"),
                        rs.getString("restaurant_name")
                )
        );
    }

    // ── 영수증 승인/거절 처리 ─────────────────────────
    public void updateVerificationStatus(Long reviewId, String status) {
        String sql = "UPDATE REVIEW SET verification_status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, reviewId);
    }

    // ── 승인/거절 처리된 영수증 목록 조회 ───────────────
    public List<ReceiptListResponse> findProcessedReceipts() {
        String sql = """
                SELECT r.id, r.content, r.rating,
                       r.verification_status, r.receipt_url,
                       r.created_at,
                       m.nickname,
                       rs.name AS restaurant_name
                FROM REVIEW r
                JOIN MEMBER m ON r.member_id = m.id
                JOIN RESTAURANT rs ON r.restaurant_id = rs.id
                WHERE r.receipt_url IS NOT NULL
                AND r.verification_status IN ('A', 'R')
                ORDER BY r.created_at DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                ReceiptListResponse.from(
                        rs.getLong("id"),
                        rs.getString("content"),
                        rs.getInt("rating"),
                        rs.getString("verification_status"),
                        rs.getString("receipt_url"),
                        rs.getTimestamp("created_at") != null
                                ? rs.getTimestamp("created_at").toLocalDateTime()
                                : null,
                        rs.getString("nickname"),
                        rs.getString("restaurant_name")
                )
        );
    }

    // ── 맛집 리뷰 목록 전체 조회 ─────────────────────────
    public List<ReviewResponse> findByRestaurantId(Long restaurantId) {

        String sql = "SELECT r.id, r.rating, r.content, r.created_at, "
                + "       r.helpful_count, r.verification_status, "
                + "       m.nickname "
                + "FROM REVIEW r "
                + "JOIN MEMBER m ON r.member_id = m.id "
                + "WHERE r.restaurant_id = ? "
                + "ORDER BY r.created_at DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        ReviewResponse.of(
                                rs.getLong("id"),
                                rs.getInt("rating"),
                                rs.getString("content"),
                                rs.getTimestamp("created_at") != null
                                        ? rs.getTimestamp("created_at").toLocalDateTime()
                                        : null,
                                rs.getString("nickname"),
                                rs.getInt("helpful_count"),
                                rs.getString("verification_status")
                        ),
                restaurantId
        );
    }

    // ── 리뷰 이미지 목록 조회 ────────────────────────────
    public List<String> findImageUrlsByReviewId(Long reviewId) {
        String sql = "SELECT image_url FROM REVIEW_IMAGE WHERE review_id = ?";
        return jdbcTemplate.queryForList(sql, String.class, reviewId);
    }

    // ── 리뷰 태그 목록 조회 ──────────────────────────────
    public List<String> findSituationsByReviewId(Long reviewId) {
        String sql = "SELECT situation FROM REVIEW_SITUATION WHERE review_id = ?";
        return jdbcTemplate.queryForList(sql, String.class, reviewId);
    }

}