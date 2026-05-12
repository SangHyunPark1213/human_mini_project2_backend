CREATE TABLE restaurant (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    address VARCHAR2(255) NOT NULL,
    phone VARCHAR2(20),
    category VARCHAR2(30) NOT NULL,
    latitude NUMBER(10,7) NOT NULL,
    longitude NUMBER(10,7) NOT NULL,
    average_rating NUMBER(2,1) DEFAULT 0,
    review_count NUMBER DEFAULT 0,
    thumbnail VARCHAR2(500),
    popular_menu VARCHAR2(255)
);

CREATE SEQUENCE restaurant_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;