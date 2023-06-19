package com.example.contest_app.controller;

import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.TokenDto;
import com.example.contest_app.domain.dto.UserDto;
import com.example.contest_app.domain.request.loginRequest;
import com.example.contest_app.domain.response.ApiResponseDto;
import com.example.contest_app.domain.response.LogoutResponse;

import com.example.contest_app.repository.UserRepository;
import com.example.contest_app.service.UserService;
import com.example.contest_app.status.ErrorStatus;
import com.example.contest_app.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/users/new-user") // 회원가입
    public ApiResponseDto<String> join(@Valid @RequestBody UserDto userDto) { // @Valid 어노테이션 추가
        try {
            if (userService.checkDuplicateEmail(userDto.getEmail())) { // 이메일 중복시
                return ApiResponseDto.error(ErrorStatus.CONFLICT_EMAIL_EXCEPTION);
            } else if (userService.checkDuplicateNickname(userDto.getNickname())) { // 닉네임 중복시
                return ApiResponseDto.error(ErrorStatus.CONFLICT_NICKNAME_EXCEPTION);
            }
            userService.save(userDto);
            return ApiResponseDto.success(SuccessStatus.SIGNUP_SUCCESS); // 회원가입 성공
        } catch (Exception e) { // 그 밖의 예외 발생시
            return ApiResponseDto.error(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login") // 로그인
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<TokenDto> login(@RequestBody @Valid final loginRequest request) {
        User user = userService.findByEmail(request.getEmail());
        if (user == null) {
            return ApiResponseDto.error(ErrorStatus.USER_CERTIFICATION_FAILED);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ApiResponseDto.error(ErrorStatus.USER_NOT_JOIN);
        }

        // 비밀번호가 일치하는 경우에만 로그인 처리
        TokenDto tokenDto = userService.login(request.getEmail(), request.getPassword());
        user.updateLoginStatus(true);
        userService.save(user);
        return ApiResponseDto.success(SuccessStatus.LOGIN_SUCCESS, tokenDto);
    }


    @GetMapping("/checkDuplicate/{nickname}") // 닉네임 중복 확인. 중복이면 true, 아니면 false
    public boolean checkDuplicateNickname(@PathVariable final String nickname) {
        return userService.checkDuplicateNickname(nickname);
    }

//    @GetMapping("/check") // 회원탈퇴, 정보 수정 시 로그인
//    public ResponseEntity<Boolean> checkLogin(@RequestParam String email, @RequestParam String password) {
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//
//        if (!optionalUser.isPresent()) {
//            return ResponseEntity.ok(false); // 유저가 없을 때
//        }
//
//        User foundUser = optionalUser.get();
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        if (!passwordEncoder.matches(password, foundUser.getPassword())) {
//            return ResponseEntity.ok(false); // 비밀번호 틀렸을 때
//        }
//
//        return ResponseEntity.ok(true); // 이메일, 비번 다 맞을 때
//    }

    @GetMapping("/check") // 회원탈퇴, 정보 수정 시 로그인
    public ApiResponseDto<String> checkLogin(@RequestParam String email, @RequestParam String password) {
        Optional<User> user = userRepository.findByEmail(email); // 이메일에 해당하는 유저 가져오기
        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            // 이메일, 비번에 해당하는 유저 없을 때
            return ApiResponseDto.error(ErrorStatus.USER_CERTIFICATION_FAILED);
        }
        return ApiResponseDto.success(SuccessStatus.CERTIFICATION_SUCCESS);
    }

//    @DeleteMapping("/delete") // 회원탈퇴
//    public ResponseEntity<String> deleteUser(HttpSession httpSession) {
//        User user = (User) httpSession.getAttribute("user");
//        try {
//            userService.deleteUser(user.getEmail());
//            httpSession.invalidate(); // 세션 무효화
//            return ResponseEntity.ok("탈퇴가 완료되었습니다.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
//        }
//    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            userService.deleteUser(email);
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

//    @PutMapping("/retouch-users") // 유저 정보 수정
//    public ApiResponseDto<EditResponse> updateUser(@RequestBody final EditRequest editRequest, HttpSession session) {
//        User user = (User) session.getAttribute("user");
//
//        if(user == null){
//            return ApiResponseDto.error(ErrorStatus.NOT_LOGIN_STATUS)
//        }
//
//        return ApiResponseDto.success()
//    }
}