package com.cheonan.matzip.dto;
//엔티티
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class Review {
    private Long id;  //리뷰번호
    private Long restaurantId;  //식당ID
    private Long memberId;  //작성자ID
    private int rating;  //별점 1~5
    private String content;  //내용
    private String revisit;  //재방문 여부(Y/N)
    private LocalDateTime createAt;  //작성일자
    private String verificationStatus;  //영수증인증상태
    private String receiptUrl;  //영수증사진
    private int helpfulCount;  //'도움돼요' 총 개수
    private int likeCount;  //'좋아요' 총 개수

    //추가: 음식 사진 리스트 (REVIEW_IMAGE 테이블과 연결될 데이터)
    //DB 조회 시 이 리스트에 음식 사진 URL들을 담아서 보냄
    private List<String> foodImageUrls;
}
