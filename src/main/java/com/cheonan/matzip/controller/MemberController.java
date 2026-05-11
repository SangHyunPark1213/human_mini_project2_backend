package com.cheonan.matzip.controller;


import com.cheonan.matzip.dto.MemberJoinRequest;
import com.cheonan.matzip.dto.MemberLoginRequest;
import com.cheonan.matzip.dto.MemberResponse;
import com.cheonan.matzip.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberJoinRequest request) {
        memberService.join(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(@RequestBody MemberLoginRequest request) {

        MemberResponse response = memberService.login(request);

        return ResponseEntity.ok(response);
    }
}