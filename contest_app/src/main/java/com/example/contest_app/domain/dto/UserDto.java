package com.example.contest_app.domain.dto;

import com.example.contest_app.domain.User;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class UserDto {
    // 회원가입 요청 DTO

    private int user_id;

    private String email; // 이메일

    private String password; // 비밀번호

    private String checkpassword; // 비밀번호 확인

    private String nickname; // 닉네임

    private boolean graduate; // 졸업 여부

    private String major1; // 전공 1

    private String major2; // 전공 2

    private boolean department;

    private boolean major_minor; // 주 부 전공

    private boolean double_major; // 복수전공

    // private boolean status; // 이메일 인증 여부

    // private String mail_key; // 인증코드


    public User toEntity(){
        return User.builder()
                .user_id(user_id)
                .password(password)
                .email(email)
                .nickname(nickname)
                .graduate(graduate)
                .major1(major1)
                .major2(major2)
                .department(department)
                .major_minor(major_minor)
                .double_major(double_major)
                .build();
    }

}
