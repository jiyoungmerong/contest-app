package com.example.contest_app.service;

import com.example.contest_app.domain.dto.UserDto;

public interface UserService {

    void save(UserDto userDto); // 회원가입

    String login(String id, String password); // 로그인

    void delete(String id); // 회원 탈퇴

    // void update(String authKey); // 인증번호 업데이트

    // void save(String authNum);

    boolean checkNicknameDuplicate(String nickname); // 닉네임 중복검사

}