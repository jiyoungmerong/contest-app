package com.example.contest_app.status;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessStatus {

    /*
    user
     */
    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입이 완료되었습니다."),

    LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공!"),

    CERTIFICATION_SUCCESS(HttpStatus.OK, "유저 인증 성공")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}