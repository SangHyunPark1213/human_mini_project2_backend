

--[REVIEW_SEQ]
--리뷰 테이블의 'id' 값 자동 1씩 증가 생성
--예: 1번 리뷰, 2번 리뷰... 순차적으로 번호 부여
CREATE SEQUENCE REVIEW_SEQ
    START WITH 1          --1부터 시작
    INCREMENT BY 1        --1씩 증가
    NOCACHE;              --미리 번호를 생성해두지 않음 (중간에 번호가 비는 현상 방지)

--[REVIEW_IMAGE_SEQ]
--리뷰 이미지 테이블의 고유 ID(PK) 생성 시퀀스
CREATE SEQUENCE REVIEW_IMAGE_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

--멤버랑 레스토랑 테이블 먼저 만들었는지 확인

--1. REVIEW (리뷰 메인 정보)
--메인 정보 테이블
CREATE TABLE REVIEW (
    id NUMBER PRIMARY KEY,                       --리뷰 고유 번호 (PK)
    restaurant_id NUMBER NOT NULL,              --어느 식당 리뷰인지 (FK)
    member_id NUMBER NOT NULL,                  --누가 작성했는지 (FK)
    rating NUMBER(1) NOT NULL,                  --별점 (1~5점만 허용)
    content VARCHAR2(1000),                      --리뷰 본문 (한글 기준 약 330자)
    revisit CHAR(1) DEFAULT 'N',                --재방문 의사 ('Y' 또는 'N'만 입력)
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,   --작성 시간 (기본값: 현재 시간)
    verification_status CHAR(1) DEFAULT 'N',    --영수증 인증 상태 (관리자 승인용)
    receipt_url VARCHAR2(500),                  --영수증 이미지 주소 (Firebase)
    like_count NUMBER DEFAULT 0,                --좋아요 카운트
    helpful_count NUMBER DEFAULT 0              --도움돼요 카운트

    --[제약 조건 설정]
    --1~5점 사이의 별점만 기록되도록 제한
    CONSTRAINT CHK_REVIEW_RATING CHECK (rating BETWEEN 1 AND 5),
    --재방문 및 인증 상태는 Y/N만 가능하도록 제한
    CONSTRAINT CHK_REVIEW_REVISIT CHECK (revisit IN ('Y', 'N')),
    CONSTRAINT CHK_REVIEW_VERIFY CHECK (verification_status IN ('Y', 'N')),
    --식당/회원 테이블과의 관계 연결 (식당이나 회원이 삭제되면 리뷰 처리 정책 결정 필요)
    CONSTRAINT FK_REVIEW_RESTAURANT FOREIGN KEY (restaurant_id) REFERENCES RESTAURANT(id),
    CONSTRAINT FK_REVIEW_MEMBER FOREIGN KEY (member_id) REFERENCES MEMBER(id),
    --[핵심] 한 명의 사용자가 같은 식당에 중복으로 리뷰를 남기지 못하게 설정
    CONSTRAINT UK_REVIEW_MEMBER_RESTAURANT UNIQUE (restaurant_id, member_id)
);

--2. REVIEW_FOOD_IMAGE (리뷰 첨부 사진)
--하나의 리뷰에 최대 3장의 사진을 담기 위해 별도로 분리한 테이블입니다. (1:N 관계)
CREATE TABLE REVIEW_IMAGE (
    id NUMBER PRIMARY KEY,                       --이미지 고유 번호 (PK)
    review_id NUMBER NOT NULL,                  --어떤 리뷰에 속한 사진인지 (FK)
    food_image_url VARCHAR2(500) NOT NULL,           --Firebase에 저장된 이미지 실제 주소

    --부모 리뷰가 삭제되면 이미지 정보도 자동으로 삭제되도록 설정
    CONSTRAINT FK_REVIEW_IMAGE_REVIEW FOREIGN KEY (review_id)
        REFERENCES REVIEW(id) ON DELETE CASCADE
);

--3. REVIEW_SITUATION (리뷰 상황 태그)
--'데이트', '가성비' 등 상황 정보를 저장합니다. (1:N 관계)
CREATE TABLE REVIEW_SITUATION (
    review_id NUMBER NOT NULL,                  --어떤 리뷰에 달린 태그인지 (FK)
    situation VARCHAR2(20) NOT NULL,            --상황 태그 값 (예: 혼밥, 회식)

    --[복합 PK] 한 리뷰 내에서 똑같은 태그가 중복 저장되는 것을 방지
    --예: 하나의 리뷰에 '가성비', '가성비' 두 번 들어가는 것 차단
    CONSTRAINT PK_REVIEW_SITUATION PRIMARY KEY (review_id, situation),
    CONSTRAINT FK_REVIEW_SIT_REVIEW FOREIGN KEY (review_id)
        REFERENCES REVIEW(id) ON DELETE CASCADE
);

--리뷰_좋아요 테이블
CREATE TABLE REVIEW_LIKE (
    review_id NUMBER,           --대상 리뷰 번호 (FK)
    member_id NUMBER,           --좋아요 누른 회원 번호 (FK)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, --누른 시간
    --(리뷰ID + 회원ID)를 묶어서 PK로 설정하여 한 명이 한 리뷰에 여러 번 누르는 것을 방지합니다.
    CONSTRAINT PK_REVIEW_LIKE PRIMARY KEY (review_id, member_id),
    CONSTRAINT FK_LIKE_REVIEW FOREIGN KEY (review_id) REFERENCES REVIEW(id),
    CONSTRAINT FK_LIKE_MEMBER FOREIGN KEY (member_id) REFERENCES MEMBER(id)
);

--리뷰_도움돼요 테이블
CREATE TABLE REVIEW_HELPFUL (
    review_id NUMBER,           --대상 리뷰 번호 (FK)
    member_id NUMBER,           --도움돼요 누른 회원 번호 (FK)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, --누른 시간
    --마찬가지로 복합 PK를 설정하여 중복 클릭을 방지합니다.
    CONSTRAINT PK_REVIEW_HELPFUL PRIMARY KEY (review_id, member_id),
    CONSTRAINT FK_HELPFUL_REVIEW FOREIGN KEY (review_id) REFERENCES REVIEW(id),
    CONSTRAINT FK_HELPFUL_MEMBER FOREIGN KEY (member_id) REFERENCES MEMBER(id)
);



--리뷰 기본 정보 저장
INSERT INTO REVIEW (id, restaurant_id, member_id, rating, content, revisit, receipt_url)
VALUES (REVIEW_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?);


--이미지 저장 (반복 호출)
INSERT INTO REVIEW_IMAGE (id, review_id, image_url)
VALUES (REVIEW_IMAGE_SEQ.NEXTVAL, ?, ?);

--상황 태그 저장 (반복 호출)
INSERT INTO REVIEW_SITUATION (review_id, situation)
VALUES (?, ?);

--특정 식당의 평균 평점과 리뷰 개수 계산(Trigger 또는 Service 호출용)
UPDATE RESTAURANT
SET average_rating = (SELECT AVG(rating) FROM REVIEW WHERE restaurant_id = ?),
    review_count = (SELECT COUNT(*) FROM REVIEW WHERE restaurant_id = ?)
WHERE id = ?;

--관리자 승인(영수증 인증)
UPDATE REVIEW
SET verification_status = 'Y'
WHERE id = ?;
