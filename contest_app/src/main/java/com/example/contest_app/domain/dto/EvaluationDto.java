package com.example.contest_app.domain.dto;


import com.example.contest_app.domain.Evaluation;
import lombok.*;

@Getter
@Setter
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


    public EvaluationDto(Evaluation evaluation) {
        this.lectureName = evaluation.getLectureName();
        this.prfsName = evaluation.getPrfsName();
        this.classYear = evaluation.getClassYear();
        this.semester = evaluation.getSemester();
        this.department = evaluation.getDepartment();
        this.teamPlay = evaluation.getTeamPlay();
        this.task = evaluation.getTask();
        this.practice = evaluation.getPractice();
        this.presentation = evaluation.getPresentation();
        this.review = evaluation.getReview();
        this.userNickname = evaluation.getNickname();
    }



}