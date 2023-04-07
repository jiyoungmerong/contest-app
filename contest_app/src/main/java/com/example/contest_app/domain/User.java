package com.example.contest_app.domain;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="User")
@AllArgsConstructor
@Builder
public class User{

    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email; // 이메일. 얘가 아이디가 될 거임

    @Column(unique = true)
    private String password; // 비밀번호

    @Column(length = 10, unique = true)
    private String nickname; // 닉네임

    private int semester; // 학기

    private boolean graduate; // 졸업 여부

    @Column(length = 15)
    private String major1; // 전공 1

    @Column(length = 15)
    private String major2; // 전공 2

    private boolean department; // 학부

    private boolean major_minor; // 주 부 전공

    private boolean double_major; // 복수전공

    private boolean isLogin;

    public String routeInfo; // 루트추천 저장


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Evaluation> evaluations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Route> routes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void addRoute(Route route) {
        routes.add(route);
        route.setUser(this);
    }

}
