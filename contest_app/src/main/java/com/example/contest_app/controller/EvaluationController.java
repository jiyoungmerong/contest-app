package com.example.contest_app.controller;

import com.example.contest_app.domain.Evaluation;
import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.EvaluationDto;
import com.example.contest_app.repository.EvaluationRepository;
import com.example.contest_app.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    private final EvaluationRepository evaluationRepository;

    @GetMapping("/posts") // 게시글 불러오기
    public ResponseEntity<Page<Evaluation>> getPosts(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<Evaluation> evaluationPage = evaluationRepository.findAll(pageable);
        return ResponseEntity.ok(evaluationPage);
    }

    @PostMapping("/save/evaluation") // 강의평 저장
    public ResponseEntity<?> createEvaluation(@RequestBody EvaluationDto evaluationDto, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        Evaluation evaluation = evaluationService.createEvaluation(evaluationDto, user);
        EvaluationDto responseDto = new EvaluationDto();
        responseDto.setLectureName(evaluation.getLectureName());
        responseDto.setPrfsName(evaluation.getPrfsName());
        responseDto.setClassYear(evaluation.getClassYear());
        responseDto.setSemester(evaluation.getSemester());
        responseDto.setDepartment(evaluation.getDepartment());
        responseDto.setTeamPlay(evaluation.getTeamPlay());
        responseDto.setTask(evaluation.getTask());
        responseDto.setPractice(evaluation.getPractice());
        responseDto.setPresentation(evaluation.getPresentation());
        responseDto.setReview(evaluation.getReview());
        responseDto.setUserNickname(evaluation.getUserNickname());
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/evaluation/{id}") // 강의평 삭제
    public ResponseEntity<String> deleteEvaluation(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다");
        }
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(id);
        if (evaluationOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("강의평을 찾을 수 없습니다.");
        }
        Evaluation evaluation = evaluationOptional.get();
        if (!evaluation.getUser().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인이 작성한 강의평만 삭제할 수 있습니다.");
        }
        evaluationRepository.delete(evaluation);
        return ResponseEntity.ok("삭제 성공");
    }

    @GetMapping("/my-evaluations")
    public ResponseEntity<?> getMyEvaluations(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        List<Evaluation> evaluations = evaluationService.getEvaluationsByUser(user);
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/evaluation/{department}") // 전공별로 불러오기
    public ResponseEntity<List<Evaluation>> getEvaluationsByDepartment(@PathVariable String department) {
        List<Evaluation> evaluations = evaluationRepository.findByDepartment(department);
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/evaluation/{lectureName}") // 강의이름으로 검색
    public ResponseEntity<?> getEvaluationsByTitle(@PathVariable String lectureName){
        List<Evaluation> evaluations = evaluationService.getEvaluationsByLecture_name(lectureName);
        return ResponseEntity.ok(evaluations);
    }
}
