package com.example.contest_app.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Evualuation")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private int id;

    private int class_year; // 수강년도

    private int semester; // 수강 학기

    @Column(length = 5)
    private String team_play; // 팀플정도

    @Column(length = 5)
    private String task; // 과제정도

    @Column(length = 5)
    private String practice; // 실습정도

    @Column(length = 5)
    private String presentation; // 발표 정도

    @Column(length = 150)
    private String review; // 총평

    private int total_star; // 총 점수


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    // 강의평가 - 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}