package com.example.contest_app.controller;

import com.example.contest_app.domain.request.EmailRequest;
import com.example.contest_app.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

//    @PostMapping("/emailConfirm")
//    public ResponseEntity emailConfirm(@RequestBody UserDto userDto) throws Exception {
//        String authCode = emailService.sendEmail(userDto.getEmail());
//        userService.update(authCode);
//        return ResponseEntity.ok("인증 완료"); // 정상적으로 작동할 시 200번 ok 발생
//
//
    @PostMapping("/emailconfirm")
    public String mailConfirm(@RequestBody EmailRequest emailRequest) throws Exception{
        String code = emailService.sendSimpleMessage(emailRequest.getEmail());
        return code;
    }
}