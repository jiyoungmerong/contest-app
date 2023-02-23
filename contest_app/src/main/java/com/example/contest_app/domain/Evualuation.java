package com.example.contest_app.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Evualuation")
public class Evualuation {

    @Id
    private int no; // 자동으로 숫자 증가

    private String lecture_name; // 강의명

    private String prfs_name; // 교수님 이름

    private String lecture_type; // 전선 전탐 전필 구분

    private int class_year; // 수강년도

    @Column(length = 5)
    private String team_play; // 팀플정도

    @Column(length = 5)
    private String task; // 과제정도

    @Column(length = 5)
    private String practice; // 실습정도

    @Column(length = 150)
    private String review; // 총평

    @Column(length = 5)
    private String total_star; // 총 점수

    private int like_count; // 추천

    private int d_like_count; // 비추천


}
