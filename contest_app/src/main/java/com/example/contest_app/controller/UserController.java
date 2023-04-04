package com.example.contest_app.controller;

import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.UserDto;
import com.example.contest_app.domain.request.EditRequest;
import com.example.contest_app.domain.request.loginRequest;
import com.example.contest_app.domain.response.EditResponse;
import com.example.contest_app.domain.response.LoginResponse;
import com.example.contest_app.domain.response.LogoutResponse;

import com.example.contest_app.repository.UserRepository;
import com.example.contest_app.service.EncryptionService;
import com.example.contest_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final EncryptionService encryptionService;

    private final UserRepository userRepository;

    @PostMapping("/users/new-user") // 회원가입
    public ResponseEntity<String> join(@Valid @RequestBody UserDto userDto) { // @Valid 어노테이션 추가
        try {
            userService.save(userDto);
            return ResponseEntity.ok("Join success"); // 메시지 명시
        } catch (DuplicateKeyException e) { // 중복된 이메일인 경우
            return ResponseEntity.badRequest().body("Join failed: Email already exists");
        } catch (Exception e) { // 그 외의 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Join failed: " + e.getMessage());
        }
    }

    @PostMapping("/users/login") // 로그인
    public ResponseEntity<LoginResponse> login(@RequestBody loginRequest request, HttpSession httpSession) {
        User user = userService.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(null);
        }

        user.setLogin(true); // 로그인 상태 업데이트
        userService.save(user); // 변경된 로그인 상태 저장

        httpSession.setAttribute("user", user); // 세션에 로그인 정보 유지

        // LoginResponse 객체 생성 및 리턴
        LoginResponse loginResponse = new LoginResponse();
        String sessionId = httpSession.getId();
        loginResponse.setSessionId(sessionId);

        loginResponse.setNickname(user.getNickname());
        loginResponse.setMajor1(user.getMajor1());
        loginResponse.setMajor2(user.getMajor2());
        loginResponse.setSemester(user.getSemester());
        loginResponse.setDepartment(user.isDepartment());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setPassword(user.getPassword());
        loginResponse.setGraduate(user.isGraduate());
        loginResponse.setMajor_minor(user.isMajor_minor());
        loginResponse.setDouble_major(user.isDouble_major());
        loginResponse.setLogin(user.isLogin());
        loginResponse.setMessage("Login Success");

        return ResponseEntity.ok(loginResponse);

    }


    @GetMapping("/checkDuplicate/{nickname}") // 닉네임 중복 확인
    public boolean checkDuplicateNickname(@PathVariable String nickname) {
        return userService.checkDuplicateNickname(nickname);
    } // 중복이면 true, 아니면 false


    @GetMapping("/check-login") // 이메일, 비밀번호 확인 (탈퇴, 회원정보수정에서 사용)
    public ResponseEntity<String> deleteLogin(@RequestBody loginRequest request) {
        // 이메일과 비밀번호가 올바른지 확인
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (!user.isPresent()) {
            // 오류 메시지 반환
            return ResponseEntity.badRequest().body("이메일과 비밀번호를 확인해주세요.");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            // 오류 메시지 반환
            return ResponseEntity.badRequest().body("이메일과 비밀번호를 확인해주세요.");
        }

        return ResponseEntity.ok().body("옳은 이메일과 비밀번호입니다");
    }
    @DeleteMapping("/delete") // 회원탈퇴
    public ResponseEntity<String> deleteUser(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        try {
            userService.deleteUser(user.getId());
            httpSession.invalidate(); // 세션 무효화
            return ResponseEntity.ok("탈퇴가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
        }
    }

    @PostMapping("/user/logout") // 로그아웃
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            session.removeAttribute("user"); // 세션에서 로그인 정보 삭제
            session.invalidate(); // 세션 무효화
            if (user != null) {
                user.setLogin(false); // 로그인 상태를 false로 변경
                userRepository.save(user); // 업데이트된 User 엔티티를 데이터베이스에 저장
            }

            // 세션 쿠키 삭제
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("JSESSIONID")) {
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                        break;
                    }
                }
            }

            LogoutResponse logoutResponse = new LogoutResponse("로그아웃 되었습니다.");
            return ResponseEntity.ok(logoutResponse);
        } else {
            LogoutResponse logoutResponse = new LogoutResponse("로그인 상태가 아닙니다.");
            return ResponseEntity.badRequest().body(logoutResponse);
        }
    }

    @PutMapping("/retouch-users") // 유저 정보 수정
    public ResponseEntity<EditResponse> updateUser(@RequestBody EditRequest editRequest, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                user.setNickname(editRequest.getNickname());
                user.setSemester(editRequest.getSemester());
                user.setGraduate(editRequest.isGraduate());
                user.setDepartment(editRequest.isDepartment());
                user.setMajor1(editRequest.getMajor1());
                user.setMajor2(editRequest.getMajor2());

                userRepository.save(user); // 업데이트된 User 엔티티를 데이터베이스에 저장

                EditResponse userResponse = new EditResponse(user);
                return ResponseEntity.ok(userResponse);
            }
        }

        EditResponse e = new EditResponse("로그인이 되어있지 않습니다.");
        return ResponseEntity.badRequest().body(e);
    }




}



