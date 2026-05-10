create table member (
   id         number primary key,
   email      varchar2(100) unique not null,
   password   varchar2(255) not null,
   nickname   varchar2(50) not null,
   role       varchar2(20) default 'USER' not null,
   created_at date default sysdate
);

create sequence member_seq start with 1 increment by 1 nocache nocycle;

select *
  from member;