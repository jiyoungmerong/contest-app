package com.example.contest_app.controller;

import com.example.contest_app.domain.request.EmailRequest;
import com.example.contest_app.service.EmailService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    private final HttpSession session;

    @PostMapping("/emailconfirm") // 인증번호 발송 버튼 누르면 메일 가게
    public ResponseEntity<String> mailConfirm(@RequestBody EmailRequest emailRequest) throws Exception{
        String code = emailService.sendSimpleMessage(emailRequest.getEmail());
        session.setAttribute("emailCode", code);
        return ResponseEntity.ok("success");
    }


    @PostMapping("/verify/{code}") // 이메일 인증하기 버튼
    public ResponseEntity<Map<String, Object>> verifyEmail(@PathVariable("code") String code, HttpSession session) {
        Map<String, Object> resultMap = new HashMap<>();
        session.setMaxInactiveInterval(30 * 60);

        String sessionCode = (String) session.getAttribute("emailCode");
        if (sessionCode != null && sessionCode.equals(code)) {
            // 인증 성공
            resultMap.put("success", true);
            return ResponseEntity.ok(resultMap);
        } else {
            // 인증 실패
            resultMap.put("success", false);
            return ResponseEntity.ok(resultMap);
        }
    }


}