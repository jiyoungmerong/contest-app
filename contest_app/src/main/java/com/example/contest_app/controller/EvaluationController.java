package com.example.contest_app.controller;

import com.example.contest_app.domain.Evaluation;
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

import javax.print.DocFlavor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    private final EvaluationRepository evaluationRepository;

    @GetMapping("/posts")
    public ResponseEntity<Page<Evaluation>> getPosts(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<Evaluation> evaluationPaga = evaluationRepository.findAll(pageable);
        return ResponseEntity.ok(evaluationPaga);
    }


    @PostMapping("/save") // + 버튼 눌렀을 때 게시글 저장
    public ResponseEntity<String> saveEvaluation(@RequestBody EvaluationDto evaluationDto, HttpSession httpSession){
        evaluationService.saveEvaluation(evaluationDto, httpSession);
        return ResponseEntity.ok("게시글 저장 성공");
    }

    @DeleteMapping("/posts/{id}") // 게시글 삭제
    public ResponseEntity<String> deletePosts(@PathVariable int id, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_Id") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        int user_id = (int) session.getAttribute("user_id");
        Optional<Evaluation> evaluation = evaluationRepository.findById(id);
        if (!evaluation.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 강의평가를 찾을 수 없습니다.");
        }
        if (evaluation.get().getUser().getUser_id() != user_id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인이 작성한 강의평가만 삭제할 수 있습니다.");
        }
        evaluationRepository.delete(evaluation.get());
        return ResponseEntity.ok("강의평가가 삭제되었습니다.");
    }

    @PutMapping("/posts/{id}") // 게시글 수정
    public ResponseEntity<String> updatePosts(@PathVariable int id, @RequestBody Evaluation updateEvaluation, HttpServletRequest request){
        HttpSession httpSession = request.getSession(false);
        if(httpSession == null || httpSession.getAttribute("user_id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        int user_id = (int) httpSession.getAttribute("user_id");
        Optional<Evaluation> evaluationOpt = evaluationRepository.findById(id);
        Evaluation evaluation = evaluationOpt.orElse(null);

        if(evaluation == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 강의평가를 찾을 수 없습니다.");
        }
        if(evaluation.getUser().getUser_id() != user_id){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인이 작성한 강의평가만 수정할 수 있습니다.");
        }
        evaluation.setClass_year(updateEvaluation.getClass_year());
        evaluation.setSemester(updateEvaluation.getSemester());
        evaluation.setTeam_play(updateEvaluation.getTeam_play());
        evaluation.setTask(updateEvaluation.getTask());
        evaluation.setPractice(updateEvaluation.getPractice());
        evaluation.setPresentation(updateEvaluation.getPresentation());
        evaluation.setReview(updateEvaluation.getReview());
        evaluation.setTotal_star(updateEvaluation.getTotal_star());
        evaluation.setLecture(updateEvaluation.getLecture());
        evaluation.setDepartment(updateEvaluation.getDepartment());
        evaluationRepository.save(evaluation);
        return ResponseEntity.ok("강의평가가 수정되었습니다.");
    }



}
