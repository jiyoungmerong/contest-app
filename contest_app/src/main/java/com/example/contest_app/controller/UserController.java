package com.example.contest_app.controller;

import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.UserDto;
import com.example.contest_app.domain.request.loginRequest;
import com.example.contest_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users/new-user") // 회원가입
    public ResponseEntity<String> join(@RequestBody UserDto userDto) {
        userService.save(userDto);
        return ResponseEntity.ok("join success");
    }


//    @PostMapping("/users/login") // 로그인
//    public ResponseEntity login(@RequestBody loginRequest request) {
//        if (userService.login(request.getId(),
//                request.getPassword()).equals("Success")) {
//            return new ResponseEntity(HttpStatus.OK);
//        }
//        return new ResponseEntity(HttpStatus.BAD_REQUEST);
//
//
//    }

    @PostMapping("/users/login") // 로그인
    public ResponseEntity<?> login(@RequestBody loginRequest request) {
        if (userService.login(request.getId(),
                request.getPassword()).equals("Success")) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }


    }
