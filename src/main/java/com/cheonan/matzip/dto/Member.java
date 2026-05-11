package com.cheonan.matzip.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Member {

    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String role;
    private LocalDateTime createdAt;
}