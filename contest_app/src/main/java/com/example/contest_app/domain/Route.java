package com.example.contest_app.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
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

    private LocalDateTime date; // 등록 시간

    private String routeInfo; // 루트 정보

    private String recommendation; // 루트 추천

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;



    public Route(String title, String department, LocalDateTime date, String recommendation, String routeInfo, User user, Lecture lecture) {
        this.title = title;
        this.department = department;
        this.date = date;
        this.recommendation = recommendation;
        this.routeInfo = routeInfo;
        this.user = user;
        this.lecture = lecture;
    }

}
