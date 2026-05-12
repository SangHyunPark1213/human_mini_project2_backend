package com.cheonan.matzip.dao;

import com.cheonan.matzip.dto.Member;
import com.cheonan.matzip.dto.request.MemberJoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public void join(MemberJoinRequest request) {
        String sql = """
                insert into member (id, email, password, nickname)
                values (member_seq.nextval, ?, ?, ?)
                """;

        jdbcTemplate.update(sql,
                request.getEmail(),
                request.getPassword(),
                request.getNickname()
        );
    }

    public Optional<Member> findByEmail(String email) {
        String sql = """
                select id, email, password, nickname, role, created_at
                from member
                where email = ?
                """;

        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setEmail(rs.getString("email"));
                member.setPassword(rs.getString("password"));
                member.setNickname(rs.getString("nickname"));
                member.setRole(rs.getString("role"));
                member.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return Optional.of(member);
            }
            return Optional.empty();
        }, email);
    }
}