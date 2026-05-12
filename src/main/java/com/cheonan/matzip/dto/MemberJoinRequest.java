package com.cheonan.matzip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinRequest {

    private String email;
    private String password;
    private String nickname;
}