package com.cheonan.matzip.service;

import com.cheonan.matzip.dao.MemberDao;
import com.cheonan.matzip.dto.Member;
import com.cheonan.matzip.dto.request.MemberJoinRequest;
import com.cheonan.matzip.dto.request.MemberLoginRequest;
import com.cheonan.matzip.dto.response.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void join(MemberJoinRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);
        memberDao.join(request);
    }

    public MemberResponse login(MemberLoginRequest request,
                                HttpServletRequest httpRequest) {
        // ① Spring Security 인증 처리 (비밀번호 검증 + 세션 생성)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // ② SecurityContext에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ③ 세션에 SecurityContext 저장 (JSESSIONID 쿠키 발급됨)
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        // ④ 사용자 정보 조회 후 반환
        Member member = memberDao.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getRole()
        );
    }
}