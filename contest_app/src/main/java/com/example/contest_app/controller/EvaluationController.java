package com.example.contest_app.controller;

import com.example.contest_app.domain.Evaluation;
import com.example.contest_app.domain.Route;
import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.EvaluationDto;
import com.example.contest_app.domain.dto.RouteDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    private final EvaluationRepository evaluationRepository;

    @GetMapping("/AllEvaluation") // 모든 강의평 불러오기
    public ResponseEntity<List<Evaluation>> getPosts(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
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

//


    // 강의평 수정@DeleteMapping("/evaluation/{id}") // 게시글 삭제
    ////    public ResponseEntity<Void> deleteEvaluation(@PathVariable int id, HttpSession session) {
    ////        User user = (User) session.getAttribute("user");
    ////        if (user == null) {
    ////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    ////        }
    ////        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(id);
    ////        if (evaluationOptional.isEmpty()) {
    ////            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    ////        }
    ////        Evaluation evaluation = evaluationOptional.get();
    ////        List<Evaluation> userEvaluations = evaluationRepository.findByUser(user);
    ////        if (!userEvaluations.contains(evaluation)) {
    ////            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    ////        }
    ////        evaluationRepository.delete(evaluation);
    ////        return ResponseEntity.noContent().build();
    ////    }

    @GetMapping("/my-evaluations")
    public ResponseEntity<List<EvaluationDto>> getAllEvaluations(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Evaluation> evaluations = evaluationRepository.findAll();
        List<EvaluationDto> responseDtos = new ArrayList<>();
        for (Evaluation evaluation : evaluations) {
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

            responseDtos.add(responseDto);
        }
        return ResponseEntity.ok(responseDtos);
    }


//    @GetMapping("/user/routes") // 자신이 작성했던 루트
//    public ResponseEntity<?> getRoutes(HttpSession httpSession) {
//
//        List<Route> routes = routeRepository.findByUserNickname(user.getNickname());
//        List<RouteDto> responseDtoList = new ArrayList<>();
//        for (Route route : routes) {
//            RouteDto responseDto = new RouteDto(route);
//            responseDto.setUserNickname(route.getUserNickname()); // 닉네임 추가
//            responseDtoList.add(responseDto);
//        }
//        return ResponseEntity.ok(responseDtoList);
//    }


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
