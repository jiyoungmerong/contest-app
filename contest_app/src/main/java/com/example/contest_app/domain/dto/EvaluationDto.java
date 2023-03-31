package com.example.contest_app.domain.dto;

import com.example.contest_app.domain.Evaluation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDto {

    private String lecture; // 강의명

    private String prfs; // 교수님

    private boolean is_major_required; // 전공 필수인지 선택인지

    private String department; // 전공 구분

    private int class_year; // 수강년도

    private int semester; // 수강 학기

    private String team_play; // 팀플유뮤

    private String task; // 과제정도

    private String practice; // 실습량

    private String presentation; // 발표 정도

    private String review; // 총평

    private int total_star; // 총 점수

//    public static EvaluationDto convertToDto(Evaluation evaluation) {
//        String prfsName = (evaluation.getPrfs() != null) ? evaluation.getPrfs().getName() : null;
//        return new EvaluationDto(
//                evaluation.getId(),
//                evaluation.getLecture(),
//                prfsName,
//                evaluation.isMajorRequired(),
//                evaluation.getDepartment(),
//                evaluation.getClassYear(),
//                evaluation.getSemester(),
//                evaluation.getTeamPlay(),
//                evaluation.getTask(),
//                evaluation.getPractice(),
//                evaluation.getPresentation(),
//                evaluation.getReview(),
//                evaluation.getTotalStar()
//        );
//    }

}