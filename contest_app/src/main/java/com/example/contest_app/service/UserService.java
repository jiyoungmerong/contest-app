package com.example.contest_app.service;

import com.example.contest_app.domain.dto.UserDto;

public interface UserService {

    void save(UserDto userDto); // 회원가입

    String login(String id, String password); // 로그인

}