package com.example.contest_app.domain;

import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="User")
@AllArgsConstructor
@Builder
public class User {

    @Id // pk
    private String id; // 학번

    @Column(unique = true)
    private String password; // 비밀번호

    private String checkpassword; // 비밀번호확인

    private String email; // 이메일

    @Column(length = 10)
    private String name; // 이름

    @Column(length = 10, unique = true)
    private String nickname; // 닉네임

    private boolean graduate; // 졸업 여부

    @Column(length = 15)
    private String major1; // 전공 1

    @Column(length = 15)
    private String major2; // 전공 2

    private boolean department; // 학부

    private boolean major_minor; // 주 부 전공

    private boolean double_major; // 복수전공

    private boolean status; // 이메일 인증 여부

    private String authNum; // 인증코드

    // public void emailVerifiedSuccess() { // 이메일 인증 진행해주는 메소드
//        this.status = true;
//    }
}
