package com.example.contest_app.service;
import com.example.contest_app.domain.Evaluation;
import com.example.contest_app.domain.Lecture;
import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.EvaluationDto;
import com.example.contest_app.repository.EvaluationRepository;
import com.example.contest_app.repository.LectureRepository;
import com.example.contest_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;

    private final UserRepository userRepository;

    private final LectureRepository lectureRepository;

    public void saveEvaluation(EvaluationDto evaluationDto, HttpSession httpSession) {
        int user_id = (int) httpSession.getAttribute("user_id");
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + user_id));
        Optional<Lecture> lectureOpt = Optional.ofNullable(lectureRepository.findByLectureAndPrfsAndDepartmentAndIsMajorRequired(
                evaluationDto.getLecture(), evaluationDto.getPrfs(), evaluationDto.getDepartment(), evaluationDto.is_major_required()));

        Lecture lecture = lectureOpt.orElseThrow(() -> new IllegalArgumentException("Invalid lecture"));

        Evaluation evaluation = Evaluation.builder()
                .user(user)
                .lecture(lecture)
                .class_year(evaluationDto.getClass_year())
                .semester(evaluationDto.getSemester())
                .team_play(evaluationDto.getTeam_play())
                .task(evaluationDto.getTask())
                .practice(evaluationDto.getPractice())
                .presentation(evaluationDto.getPresentation())
                .review(evaluationDto.getReview())
                .total_star(evaluationDto.getTotal_star())
                .build();

        evaluationRepository.save(evaluation);
    }

    public Page<Evaluation> getEvaluationList(Pageable pageable){

        return evaluationRepository.findAll(pageable);
    }

    public Page<Evaluation> getEvaluationsByDepartment(String department, Pageable pageable) {
        return evaluationRepository.findByLecture_Department(department, pageable);
    }

    public Page<Evaluation> findByDepartmentContaining(String department, Pageable pageable) {
        return evaluationRepository.findByLecture_DepartmentContaining(department, pageable);
    }


}
