package com.example.contest_app.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvaluationDto {


    private String department_name;

    private String lecture;

    private String prfs;

    private boolean is_major_required;

    private int class_year; // 수강년도

    private int semester; // 수강 학기

    private String team_play; // 팀플유뮤

    private String task; // 과제정도

    private String practice; // 실습량

    private String presentation; // 발표 정도

    private String review; // 총평

    private String total_star; // 총 점수


}