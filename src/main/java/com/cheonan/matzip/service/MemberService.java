package com.cheonan.matzip.service;

import com.cheonan.matzip.dao.MemberDao;
import com.cheonan.matzip.dto.Member;
import com.cheonan.matzip.dto.MemberJoinRequest;
import com.cheonan.matzip.dto.MemberLoginRequest;
import com.cheonan.matzip.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;

    public void join(MemberJoinRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);

        memberDao.join(request);
    }

    public MemberResponse login(MemberLoginRequest request) {
        Member member = memberDao.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getRole()
        );
    }
}