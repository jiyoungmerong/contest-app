package com.example.contest_app.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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

    private LocalDate date; // 등록 시간

    @Column(nullable = false, length = 100)
    private String recommendation; // 루트 추천

    @Column(length = 1000)
    private String routeInfo; // 루트 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    public Route(String title, String department, LocalDate date, String recommendation, String routeInfo, User user, Lecture lecture) {
        this.title = title;
        this.department = department;
        this.date = date;
        this.recommendation = recommendation;
        this.routeInfo = routeInfo;
        this.user = user;
        this.lecture = lecture;
    }

}
