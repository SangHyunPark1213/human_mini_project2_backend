CREATE SEQUENCE member_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

-- 위 시퀀스와 동일한 이름을 가진 테이블이 존재하므로 시퀀스 생성 전 삭제 필요
-- SELECT object_name, object_type
-- FROM user_objects
-- WHERE object_name = 'MEMBER_SEQ';

-- DROP TABLE member_seq;