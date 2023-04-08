package com.example.contest_app.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EvaluationEditRequest {
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
}
