package com.example.contest_app.controller;

import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.TokenDto;
import com.example.contest_app.domain.dto.UserDto;
import com.example.contest_app.domain.request.EditRequest;
import com.example.contest_app.domain.request.loginRequest;
import com.example.contest_app.domain.response.ApiResponseDto;
import com.example.contest_app.domain.response.EditResponse;
import com.example.contest_app.domain.response.LoginResponse;
import com.example.contest_app.domain.response.LogoutResponse;

import com.example.contest_app.jwt.TokenProvider;
import com.example.contest_app.repository.UserRepository;
import com.example.contest_app.service.UserService;
import com.example.contest_app.status.ErrorStatus;
import com.example.contest_app.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;


    @PostMapping("/users/new-user") // 회원가입
    public ApiResponseDto<String> join(@Valid @RequestBody UserDto userDto) { // @Valid 어노테이션 추가
        try{
            if(userService.checkDuplicateEmail(userDto.getEmail())){
                return ApiResponseDto.error(ErrorStatus.CONFLICT_EMAIL_EXCEPTION);
            }
            else if(userService.checkDuplicateNickname(userDto.getNickname())){
                return ApiResponseDto.error(ErrorStatus.CONFLICT_NICKNAME_EXCEPTION);
            }
            userService.save(userDto);
            return ApiResponseDto.success(SuccessStatus.SIGNUP_SUCCESS);
        } catch (Exception e){
            return ApiResponseDto.error(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<TokenDto> login(@RequestBody @Valid final loginRequest request){
        TokenDto tokenDto = userService.login(request.getEmail(), request.getPassword());
        User user = userService.findByEmail(request.getEmail());
        user.updateLoginStatus(true);
        userService.save(user);
        return ApiResponseDto.success(SuccessStatus.LOGIN_SUCCESS, tokenDto);
    } // 추후 구현 : isLogin 상태 true로 업데이트

//    @PostMapping("/users/login") // 로그인
//    public Map<String, Object> login(@RequestBody loginRequest request, HttpSession httpSession, HttpServletResponse response) {
//        User user = userService.findByEmail(request.getEmail());

//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            responseBody.put("status", "error");
//            responseBody.put("message", "Invalid password");
//            return responseBody;
//        }
//
//        user.setLogin(true); // 로그인 상태 업데이트
//        userService.save(user); // 변경된 로그인 상태 저장
//
//
//        responseBody.put("sessionId", httpSession.getId());
//        responseBody.put("nickname", user.getNickname());
//        responseBody.put("major1", user.getMajor1());
//        responseBody.put("major2", user.getMajor2());
//        responseBody.put("semester", user.getSemester());
//        responseBody.put("department", user.isDepartment());
//        responseBody.put("email", user.getEmail());
//        responseBody.put("password", user.getPassword());
//        responseBody.put("graduate", user.isGraduate());
//        responseBody.put("major_minor", user.isMajor_minor());
//        responseBody.put("double_major", user.isDouble_major());
//        responseBody.put("login", user.isLogin());
//        responseBody.put("message", "Login Success");
//
//        return responseBody;
//    }

    @GetMapping("/checkDuplicate/{nickname}") // 닉네임 중복 확인
    public boolean checkDuplicateNickname(@PathVariable String nickname) {
        return userService.checkDuplicateNickname(nickname);
    } // 중복이면 true, 아니면 false


    @GetMapping("/check-login")
    public ResponseEntity<Boolean> checkLogin(@RequestParam String email, @RequestParam String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (!optionalUser.isPresent()) {
            return ResponseEntity.ok(false); // 유저가 없을 때
        }

        User foundUser = optionalUser.get();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, foundUser.getPassword())) {
            return ResponseEntity.ok(false); // 비밀번호 틀렸을 때
        }

        return ResponseEntity.ok(true); // 이메일, 비번 다 맞을 때
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
    public ResponseEntity<EditResponse> updateUser(@RequestBody EditRequest editRequest, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            user.setNickname(editRequest.getNickname());
            user.setSemester(editRequest.getSemester());
            user.setGraduate(editRequest.isGraduate());
            user.setDepartment(editRequest.isDepartment());
            user.setMajor1(editRequest.getMajor1());
            user.setMajor2(editRequest.getMajor2());
            user.setMajor_minor(editRequest.isMajor_minor());
            user.setDouble_major(editRequest.isDouble_major());

            userRepository.save(user); // 업데이트된 User 엔티티를 데이터베이스에 저장

            EditResponse userResponse = new EditResponse(user);
            return ResponseEntity.ok(userResponse);
        }

        EditResponse e = new EditResponse("로그인이 되어있지 않습니다.");
        return ResponseEntity.badRequest().body(e);
    }


}



