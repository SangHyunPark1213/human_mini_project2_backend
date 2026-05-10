package com.cheonan.matzip.controller;

import com.cheonan.matzip.dto.MemberJoinRequest;
import com.cheonan.matzip.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public String join(
            @RequestBody MemberJoinRequest request
    ) {

        memberService.join(request);

        return "회원가입 성공";
    }
}