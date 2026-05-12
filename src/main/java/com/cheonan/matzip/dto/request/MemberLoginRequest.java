package com.cheonan.matzip.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginRequest {
    private String email;
    private String password;
}