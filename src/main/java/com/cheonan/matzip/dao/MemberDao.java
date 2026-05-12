package com.cheonan.matzip.dao;

import com.cheonan.matzip.dto.MemberJoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public void join(MemberJoinRequest request) {

        String sql = """
                INSERT INTO member (
                    id,
                    email,
                    password,
                    nickname,
                    role
                )
                VALUES (
                    member_seq.NEXTVAL,
                    ?,
                    ?,
                    ?,
                    'USER'
                )
                """;

        jdbcTemplate.update(
                sql,
                request.getEmail(),
                request.getPassword(),
                request.getNickname()
        );
    }
}