package com.example.contest_app.controller;

import com.example.contest_app.domain.request.EmailRequest;
import com.example.contest_app.service.EmailService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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


    @PostMapping("/verify")
    public String verifyEmail(@RequestParam("code") String code) {
        String sessionCode = (String) session.getAttribute("emailCode");
        if (sessionCode != null && sessionCode.equals(code)) {
            // 인증 성공
            return "Success";
        } else {
            // 인증 실패
            return "Fail";
        }
    }



}