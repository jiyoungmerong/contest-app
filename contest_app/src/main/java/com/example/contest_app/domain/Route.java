package com.example.contest_app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="Route")
@AllArgsConstructor
@Builder
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title; // 제목

    @Column(nullable = false)
    private String department; // 전공

    private LocalDateTime createAt; // 등록 시간

    private String userNickname;

    private String routeInfo; // 루트 정보

    private String recommendation; // 루트 추천

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("routes")
    private User user;


}
