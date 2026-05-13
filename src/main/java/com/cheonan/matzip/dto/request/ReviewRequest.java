package com.cheonan.matzip.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewRequest {
    private Long restaurantId;
    private int rating;
    private Long memberId;
    private String content;
    private String revisit;
    private String receiptUrl;
<<<<<<< HEAD

    private List<String> foodImageUrls;
=======
    private List<String> imageUrls;
>>>>>>> origin/develop
    private List<String> situations;
}

@Getter
@Setter
class ReviewInteractionRequest {
    private Long reviewId;  //어떤 리뷰에 눌렀나?
    //"LIKE" 인지 "HELPFUL" 인지 구분, 각각 별개의 요청으로 처리되어 클릭하는 순간 서버로 보냄
    private String interactionType;
}
