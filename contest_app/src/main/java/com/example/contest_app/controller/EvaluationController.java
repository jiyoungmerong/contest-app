package com.example.contest_app.controller;

import com.example.contest_app.domain.Evaluation;
import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.EvaluationDto;
import com.example.contest_app.domain.request.EvaluationEditRequest;
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
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    private final EvaluationRepository evaluationRepository;

    @GetMapping("/AllEvaluation") // 모든 강의평 불러오기
    public ResponseEntity<List<Evaluation>> getPosts(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Evaluation> evaluationPage = evaluationRepository.findAll(pageable);
        List<Evaluation> evaluationList = evaluationPage.getContent();
        return ResponseEntity.ok(evaluationList);
    }

    @PostMapping("/save/evaluation") // 강의평 저장
    public ResponseEntity<?> createEvaluation(@RequestBody EvaluationDto evaluationDto, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        Evaluation evaluation = evaluationService.createEvaluation(evaluationDto, user);
        EvaluationDto responseDto = new EvaluationDto();
        responseDto.setId(evaluation.getId()); // id 추가
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
        responseDto.setUserNickname(evaluation.getNickname());
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/my-evaluations") // 내가 작성한 강의평 불러오기 ( ID 추가 )
    public ResponseEntity<List<EvaluationDto>> getAllEvaluations(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Evaluation> evaluations = evaluationRepository.findAllByNickname(user.getNickname()); // 수정된 부분
        List<EvaluationDto> responseDtos = new ArrayList<>();
        for (Evaluation evaluation : evaluations) {
            EvaluationDto responseDto = new EvaluationDto();
            responseDto.setId(evaluation.getId()); // id 추가
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
            responseDto.setUserNickname(evaluation.getNickname());

            responseDtos.add(responseDto);
        }
        return ResponseEntity.ok(responseDtos);
    }


    @GetMapping("/evaluations/{id}") // 해당 id의 강의평
    public ResponseEntity<EvaluationDto> getEvaluationById(@PathVariable int id) {
        Evaluation evaluation = evaluationService.getEvaluationById(id);
        if (evaluation == null) {
            return ResponseEntity.notFound().build();
        }
        EvaluationDto responseDto = new EvaluationDto();
        responseDto.setId(evaluation.getId()); // id 추가
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
        responseDto.setUserNickname(evaluation.getNickname());
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/evaluation/{evaluation_id}") // 강의평 수정
    public ResponseEntity<EvaluationDto> updateEvaluation(@PathVariable int evaluation_id, @Valid @RequestBody EvaluationEditRequest evaluationEditRequest,
                                                          HttpSession session) {
        User user = (User) session.getAttribute("user");

        Optional<Evaluation> optionalEvaluation = evaluationRepository.findById(evaluation_id);
        if (optionalEvaluation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Evaluation evaluation = optionalEvaluation.get();

        if (evaluationEditRequest.getLectureName() != null) {
            evaluation.setLectureName(evaluationEditRequest.getLectureName());
        }
        else{
            evaluation.setNickname(evaluation.getNickname());
        }

        if (evaluationEditRequest.getPrfsName() != null) {
            evaluation.setPrfsName(evaluationEditRequest.getPrfsName());
        }
        else{
            evaluation.setPrfsName(evaluation.getPrfsName());
        }

        if (evaluationEditRequest.getClassYear() != 0) {
            evaluation.setClassYear(evaluationEditRequest.getClassYear());
        }
        else{
            evaluation.setClassYear(evaluation.getClassYear());
        }

        if (evaluationEditRequest.getSemester() != 0) {
            evaluation.setSemester(evaluationEditRequest.getSemester());
        }
        else{
            evaluation.setSemester(evaluation.getSemester());
        }

        if (evaluationEditRequest.getDepartment() != null) {
            evaluation.setDepartment(evaluationEditRequest.getDepartment());
        }
        else{
            evaluation.setDepartment(evaluation.getDepartment());
        }

        if (evaluationEditRequest.getTeamPlay() != 0) {
            evaluation.setTeamPlay(evaluationEditRequest.getTeamPlay());
        }
        else{
            evaluation.setTeamPlay(evaluation.getTeamPlay());
        }

        if (evaluationEditRequest.getTask() != 0) {
            evaluation.setTask(evaluationEditRequest.getTask());
        }
        else{
            evaluation.setTask(evaluation.getTask());
        }

        if (evaluationEditRequest.getPractice() != 0) {
            evaluation.setPractice(evaluationEditRequest.getPractice());
        }
        else{
            evaluation.setPresentation(evaluation.getPresentation());
        }

        if (evaluationEditRequest.getPresentation() != 0) {
            evaluation.setPresentation(evaluationEditRequest.getPresentation());
        }
        else{
            evaluation.setPresentation(evaluation.getPresentation());
        }

        if (evaluationEditRequest.getReview() != null) {
            evaluation.setReview(evaluationEditRequest.getReview());
        }
        else{
            evaluation.setReview(evaluation.getReview());
        }

        evaluationRepository.save(evaluation);
        EvaluationDto evaluationDto = new EvaluationDto(evaluation);
        evaluationDto.setUserNickname(user.getNickname()); // EvaluationDto 객체의 userNickname 필드 값을 설정해줍니다.

        return ResponseEntity.ok(new EvaluationDto(evaluation));
    }

    @DeleteMapping("/evaluation/{evaluation_id}") // 루트글 삭제
    public ResponseEntity<?> deleteRoute(@PathVariable("evaluation_id") int evaluation_id, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to access this resource.");
        }

        Optional<Evaluation> optionalEvaluation = evaluationRepository.findById(evaluation_id);

        if (optionalEvaluation.isPresent()) {
            Evaluation evaluation = optionalEvaluation.get();

            // 사용자가 작성한 루트인지 확인
            if (!evaluation.getNickname().equals(user.getNickname())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this route.");
            }

            evaluationRepository.delete(evaluation);
            return ResponseEntity.ok("evaluation deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("evaluation not found.");
        }
    }

    @GetMapping("/evaluation/department") // 전공별로 불러오기
    public ResponseEntity<List<Evaluation>> getEvaluationsByDepartment(@RequestParam String department) {
        List<Evaluation> evaluations = evaluationRepository.findByDepartment(department);
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/evaluation/lectureName") // 강의이름으로 검색
    public ResponseEntity<?> getEvaluationsByTitle(@RequestParam String lectureName){
        List<Evaluation> evaluations = evaluationService.getEvaluationsByLecture_name(lectureName);
        return ResponseEntity.ok(evaluations);
    }
}
