package com.example.contest_app.service;
import com.example.contest_app.domain.Evaluation;
import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.EvaluationDto;
import com.example.contest_app.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;

    public Evaluation createEvaluation(EvaluationDto evaluationDto, User user) {
        Evaluation evaluation = new Evaluation();
        evaluation.setLectureName(evaluationDto.getLectureName());
        evaluation.setPrfsName(evaluationDto.getPrfsName());
        evaluation.setClassYear(evaluationDto.getClassYear());
        evaluation.setSemester(evaluationDto.getSemester());
        evaluation.setDepartment(evaluationDto.getDepartment());
        evaluation.setTeamPlay(evaluationDto.getTeamPlay());
        evaluation.setTask(evaluationDto.getTask());
        evaluation.setPractice(evaluationDto.getPractice());
        evaluation.setPresentation(evaluationDto.getPresentation());
        evaluation.setReview(evaluationDto.getReview());
        evaluation.setNickname(user.getNickname());


        return evaluationRepository.save(evaluation);
    }


    public List<Evaluation> getEvaluationsByLecture_name(String title) {
        return evaluationRepository.findByLectureName(title);
    }


    public Evaluation getEvaluationById(int evaluation_id) {
        return evaluationRepository.findById(evaluation_id).orElse(null);
    }


}
