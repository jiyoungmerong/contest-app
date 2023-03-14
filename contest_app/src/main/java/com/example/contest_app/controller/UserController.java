package com.example.contest_app.controller;

import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.UserDto;
import com.example.contest_app.domain.request.loginRequest;
import com.example.contest_app.exception.InvalidPasswordException;
import com.example.contest_app.exception.UserNotFoundException;
import com.example.contest_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.file.attribute.UserPrincipal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users/new-user") // 회원가입
    public ResponseEntity<String> join(@RequestBody UserDto userDto) {
        if(!userDto.getPassword().equals(userDto.getCheckpassword())){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } // 비밀번호와 확인용 비밀번호가 맞지 않을 경우, 400 Bad 발생시킴

        else{
            userService.save(userDto);
        }
        return ResponseEntity.ok("join success"); // 정상적으로 작동할 시 200번 ok 발생
    }

    @PostMapping("/users/login") // 로그인
    public ResponseEntity<Boolean> login(@RequestBody loginRequest request, HttpSession httpSession) {
        boolean loginSuccess = Boolean.parseBoolean(String.valueOf(userService.login(request.getEmail(), request.getPassword()).equals("true")));

        if (loginSuccess){
            User user = userService.findByEmail(request.getEmail());
            httpSession.setAttribute("user", user); // 세션에 로그인 정보 유지
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

//    @GetMapping("/users/me") // 로그인한 사용자 정보 조회
//    public ResponseEntity<User> getLoggedInUser(HttpSession httpSession) {
//        User user = (User) httpSession.getAttribute("user");
//        if (user != null) {
//            return new ResponseEntity<User>(user, HttpStatus.OK);
//        }
//        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
//    }

    @DeleteMapping("/delete") // 회원 탈퇴
    public ResponseEntity<String> deleteUser(@RequestParam("password") String password, Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            int currentUserId = currentUser.getUser_id();
            userService.deleteUser(currentUserId, password);
            return ResponseEntity.ok("사용자가 성공적으로 삭제되었습니다.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // 현재 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // 새로운 세션 생성
        HttpSession newSession = request.getSession(true);
        // 새로운 세션 ID 얻기
        String newSessionId = newSession.getId();
        // 로그아웃 메시지와 새로운 세션 ID 전송
        return ResponseEntity.ok("로그아웃 되었습니다. 새로운 세션 ID: " + newSessionId);
    }
}
