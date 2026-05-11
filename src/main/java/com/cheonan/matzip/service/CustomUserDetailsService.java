package com.cheonan.matzip.service;

import com.cheonan.matzip.dao.MemberDao;
import com.cheonan.matzip.dto.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberDao memberDao;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Member member = memberDao.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("이메일 또는 비밀번호가 일치하지 않습니다")
                );

        // Spring Security가 인식하는 권한 형식: "ROLE_USER", "ROLE_ADMIN"
        String role = "ROLE_" + member.getRole(); // "USER" → "ROLE_USER"

        return new User(
                member.getEmail(),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}