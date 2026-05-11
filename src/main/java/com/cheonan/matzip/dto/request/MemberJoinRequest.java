package com.cheonan.matzip.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinRequest {
    private String email;
    private String password;
    private String nickname;
}