package com.example.contest_app.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String sessionId;

    private String nickname;

    private String major1;

    private String major2;

    private int semester;

    private boolean department; // 학부

    private String email; // 이메일. 얘가 아이디가 될 거임

    private String password; // 비밀번호

    private boolean graduate; // 졸업 여부

    private boolean major_minor; // 주 부 전공

    private boolean double_major; // 복수전공

    private boolean isLogin;

    private String message; // 로그인 메세지


}
