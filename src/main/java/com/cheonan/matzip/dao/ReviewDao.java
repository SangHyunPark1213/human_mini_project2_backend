package com.cheonan.matzip.dao;

import com.cheonan.matzip.dto.ReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewDao {

    private final JdbcTemplate jdbcTemplate;

    public Long insertReview(ReviewRequest req) {
        String sql ="INSERT INTO REVIEW(id, restaurant_id, member_id, rating, content, revisit, receipt_url)"
                + "VALUES(REVIEW_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";


    //데이터 채우기
    jdbcTemplate.update(sql,
                        req.getRestaurantId(),
                        req.getMemberId(),
                        req.getRating(),
                        req.getContent(),
                        req.getRevisit(),
                        req.getReceiptUrl());

    return jdbcTemplate.queryForObject("SELECT REVIEW_SEQ.CURRVAL FROM DUAL", Long.class);


    }

    //리뷰 이미지 저장 (최대 3개)
    public void insertReviewImage(Long reviewId, String imageUrl) {
        String sql = "INSERT INTO REVIEW_IMAGE(id, review_id, image_url)" +
                    "VALUES (?, ?)";
        jdbcTemplate.update(sql, reviewId, imageUrl);
    }

    pu



}
