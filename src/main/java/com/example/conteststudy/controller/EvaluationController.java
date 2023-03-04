package com.example.conteststudy.controller;

import com.example.conteststudy.domain.dto.EvaluationDto;
import com.example.conteststudy.domain.dto.UserDto;
import com.example.conteststudy.service.EvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping("/evaluations/save")
    public ResponseEntity<String> join(@RequestBody EvaluationDto evaluationDto) {
        evaluationService.save(evaluationDto);
        return ResponseEntity.ok("save success");
    }


}
