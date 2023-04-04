package com.example.contest_app.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class loginRequest { // 로그인요청
    private String email;
    private String password;
}
