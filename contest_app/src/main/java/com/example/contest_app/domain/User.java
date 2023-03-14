package com.example.contest_app.domain;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="User")
@AllArgsConstructor
@Builder
public class User {

    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @Column(unique = true)
    private String password; // 비밀번호

    private String email; // 이메일. 얘가 아이디가 될 거임


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

    private String authNum; // 인증코드

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluation> evaluations = new ArrayList<>();

}
