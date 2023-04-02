package com.example.contest_app.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="User")
@AllArgsConstructor
@Builder
public class User implements UserDetails {

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

    private boolean isEmailVerified; // 이메일 인증 되었는지

    private boolean isLogin;

    @Column(length = 1000)
    public String routeInfo; // 루트추천 저장

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluation> evaluations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Route> routes = new ArrayList<>();


    public void addRoute(Route route) {
        routes.add(route);
        route.setUser(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
