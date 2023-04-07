package com.example.contest_app.domain.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class EvaluationDto {


    private String lectureName; // 강의 이름

    private String prfsName; // 교수 이름

    private int classYear; // 수강년도

    private int semester; // 수강 학기

    private String department; // 전공 구분

    private int teamPlay; // 팀플유뮤

    private int task; // 과제정도

    private int practice; // 실습량

    private int presentation; // 발표 정도

    private String review; // 총평

    private String userNickname; // 유저의 닉네임을 저장하기 위한 필드


    public EvaluationDto(String lectureName, String prfsName, int classYear, int semester,
                         String department, int teamPlay, int task, int practice,
                         int presentation, String review, String userNickname) {
        this.lectureName = lectureName;
        this.prfsName = prfsName;
        this.classYear = classYear;
        this.semester = semester;
        this.department = department;
        this.teamPlay = teamPlay;
        this.task = task;
        this.practice = practice;
        this.presentation = presentation;
        this.review = review;
        this.userNickname = userNickname;
    }


}