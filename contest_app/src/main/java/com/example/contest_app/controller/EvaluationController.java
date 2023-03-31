package com.example.contest_app.controller;

import com.example.contest_app.domain.Evaluation;
import com.example.contest_app.domain.Lecture;
import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.EvaluationDto;
import com.example.contest_app.repository.EvaluationRepository;
import com.example.contest_app.repository.LectureRepository;
import com.example.contest_app.repository.UserRepository;
import com.example.contest_app.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    private final EvaluationRepository evaluationRepository;

    private final LectureRepository lectureRepository;

    private final UserRepository userRepository;

    @GetMapping("/posts") // 게시글 불러오기
    public ResponseEntity<Page<Evaluation>> getPosts(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<Evaluation> evaluationPage = evaluationRepository.findAll(pageable);
        return ResponseEntity.ok(evaluationPage);
    }

    @PostMapping("/save") // + 버튼 눌렀을 때 게시글 저장
    public ResponseEntity<String> createEvaluation(@RequestBody EvaluationDto evaluationDto, HttpServletRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // 로그인되어 있지 않은 경우, 로그인 페이지로 이동하도록 리다이렉트
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("로그인이 필요합니다.");
        }
        try {
            evaluationService.saveEvaluation(evaluationDto, request.getSession());
            return ResponseEntity.ok("게시글 저장 성공");
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 저장 실패: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 저장 실패: " + e.getMessage());
        }
    }


    @DeleteMapping("/posts/{id}") // 게시글 삭제
    public ResponseEntity<String> deletePosts(@PathVariable int id, HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session.getAttribute("id") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        int userId = (int) session.getAttribute("id");
        Optional<Evaluation> evaluation = evaluationRepository.findById(id);
        if (!evaluation.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 강의평가를 찾을 수 없습니다.");
        }
        if (evaluation.get().getUser().getId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인이 작성한 강의평가만 삭제할 수 있습니다.");
        }
        evaluationRepository.delete(evaluation.get());
        return ResponseEntity.ok("강의평가가 삭제되었습니다.");
    }

    @PutMapping("/posts/{id}") // 게시글 수정
    public ResponseEntity<String> updateEvaluation(@PathVariable int id, @RequestBody EvaluationDto evaluationDto, HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if(httpSession == null || httpSession.getAttribute("user_id") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        int user_id = (int) httpSession.getAttribute("user_id");
        Optional<Evaluation> evaluationOpt = evaluationRepository.findById(id);
        Evaluation evaluation = evaluationOpt.orElse(null);
        if(evaluation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 강의평가를 찾을 수 없습니다.");
        }
        if(evaluation.getUser().getId() != user_id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인이 작성한 강의평가만 수정할 수 있습니다.");
        }
        Lecture lecture = lectureRepository.findByLectureAndPrfsAndDepartmentAndIsMajorRequired(
                evaluationDto.getLecture(), evaluationDto.getPrfs(), evaluationDto.getDepartment(), evaluationDto.is_major_required());

        evaluation.setLecture(lecture);
        evaluation.setClass_year(evaluationDto.getClass_year());
        evaluation.setSemester(evaluationDto.getSemester());
        evaluation.setTeam_play(evaluationDto.getTeam_play());
        evaluation.setTask(evaluationDto.getTask());
        evaluation.setPractice(evaluationDto.getPractice());
        evaluation.setPresentation(evaluationDto.getPresentation());
        evaluation.setReview(evaluationDto.getReview());
        evaluation.setTotal_star(evaluationDto.getTotal_star());

        evaluationRepository.save(evaluation);
        return ResponseEntity.ok("강의평가가 수정되었습니다.");
    }

//    @GetMapping("/my-posts")
//    public ResponseEntity<List<EvaluationDto>> getMyPosts(Authentication authentication) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//
//        String email = authentication.getName();
//        List<Evaluation> evaluations = evaluationRepository.findByUserEmail(email);
//
//        List<EvaluationDto> evaluationDtos = new ArrayList<>();
//        for (Evaluation evaluation : evaluations) {
//            EvaluationDto dto = new EvaluationDto();
//            dto.setId(evaluation.getId());
//            dto.setProfessor(evaluation.getProfessor());
//            dto.setCourse(evaluation.getCourse());
//            dto.setSemester(evaluation.getSemester());
//            dto.setYear(evaluation.getYear());
//            dto.setContent(evaluation.getContent());
//            dto.setCreatedAt(evaluation.getCreatedAt());
//            evaluationDtos.add(dto);
//        }
//
//        return ResponseEntity.ok(evaluationDtos);
//    }

//    @GetMapping("/department/{department}") // 전공별로 검색
//    public ResponseEntity<Page<EvaluationDto>> findByDepartmentContaining(
//            @PathVariable String department,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Evaluation> evaluations = evaluationService.findByDepartmentContaining(department, pageable);
//        Page<EvaluationDto> evaluationDtos = evaluations.map(EvaluationDto::convertToDto);
//        return ResponseEntity.ok(evaluationDtos);
//    }
    @GetMapping("evaluation/{name}") // 강의평 제목으로 검색
    public ResponseEntity<Page<Evaluation>> searchReviews(@PathVariable String name,
                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "10") int size) {
        List<Lecture> lectures = lectureRepository.findByNameContaining(name);
        List<Integer> courseIds = lectures.stream().map(Lecture::getId).collect(Collectors.toList());
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Evaluation> evaluations = evaluationRepository.findAllByLectureIdIn(courseIds, pageable);

        return ResponseEntity.ok(evaluations);
    }

}
