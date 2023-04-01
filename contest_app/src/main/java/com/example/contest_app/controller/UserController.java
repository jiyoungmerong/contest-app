package com.example.contest_app.controller;

import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.UserDto;
import com.example.contest_app.domain.request.DeleteRequest;
import com.example.contest_app.domain.request.loginRequest;
import com.example.contest_app.domain.response.LogoutResponse;
import com.example.contest_app.exception.EncryptionException;
import com.example.contest_app.exception.InvalidPasswordException;
import com.example.contest_app.exception.UserNotFoundException;
import com.example.contest_app.repository.UserRepository;
import com.example.contest_app.service.EncryptionService;
import com.example.contest_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<String> login(@RequestBody loginRequest request, HttpSession httpSession) {
        User user = userService.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.badRequest().body("이메일과 비밀번호를 확인해주세요.");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Incorrect password");
        }

        httpSession.setAttribute("user", user); // 세션에 로그인 정보 유지
        return ResponseEntity.ok("Login success");
    }


    @PostMapping("/check-login") // 회원정보수정창 로그인
    public ResponseEntity<String> checkLogin(@RequestBody loginRequest request) {
        // 이메일과 비밀번호가 올바른지 확인
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if (user == null) {
            // 오류 메시지 반환
            return ResponseEntity.badRequest().body("이메일과 비밀번호를 확인해주세요.");
        }
        // 회원정보 수정 페이지로 이동
        return ResponseEntity.ok().body("회원정보 수정 페이지로 이동합니다.");
    }

    @GetMapping("/checkDuplicate/{nickname}") // 닉네임 중복 확인
    public boolean checkDuplicateNickname(@PathVariable String nickname) {
        return userService.checkDuplicateNickname(nickname);
    } // 중복이면 true, 아니면 false


    @DeleteMapping("/delete") // 회원탈퇴
    public ResponseEntity<String> deleteUser(@Valid @RequestBody DeleteRequest deleteRequest, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        int currentUserId = currentUser.getId();

        String password = deleteRequest.getPassword();

        if (password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body("비밀번호를 입력해주세요.");
        }

        try {
            String encryptedPassword = encryptionService.encrypt(password); // 비밀번호 암호화
            userService.deleteUser(currentUserId, encryptedPassword);
            return ResponseEntity.ok("사용자가 성공적으로 삭제되었습니다.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("해당 사용자를 찾을 수 없습니다.");
        } catch (InvalidPasswordException e) {
            return ResponseEntity.badRequest().body("잘못된 비밀번호입니다.");
        } catch (EncryptionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 암호화에 실패했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
        }
    }

    // 쿠키를 사용하여 새로운 세션 ID를 클라이언트에게 전달.
    // 클라이언트는 이 새로운 세션 ID를 이용하여 새로운 세션을 시작할 수 있음
    @PostMapping("/logout") // 로그아웃
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        // 현재 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // 새로운 세션 생성
        HttpSession newSession = request.getSession(true);
        // 새로운 세션 ID 얻기
        String newSessionId = newSession.getId();
        // 클라이언트에게 전달할 새로운 세션 ID를 쿠키에 저장
        Cookie cookie = new Cookie("JSESSIONID", newSessionId);
        cookie.setPath("/");
        cookie.setMaxAge(-1); // 브라우저를 닫을 때 쿠키 삭제
        response.addCookie(cookie);
        // 로그아웃 메시지와 새로운 세션 ID 전송
        LogoutResponse logoutResponse = new LogoutResponse("로그아웃 되었습니다.", newSessionId);
        return ResponseEntity.ok(logoutResponse);
    }
    @GetMapping("/user-profile") // 사용자 정보 가져오기
    @ResponseBody
    public ResponseEntity<UserDto> getMyProfile() {
        try {
            // 현재 로그인된 사용자의 정보 불러오기
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            Optional<User> user = userRepository.findByEmail(email);
            UserDto userdto = UserDto.convertToDto(Optional.ofNullable(user.orElseThrow(() -> new Exception("User not found"))));

            // 사용자 정보를 UserDto로 변환하여 전송
            UserDto u = UserDto.convertToDto(user);
            return ResponseEntity.ok(u);
        } catch (Exception e) {
            // 예외 발생 시 500에러 응답 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/user-profile") // 유저 정보 수정
    @ResponseBody
    public ResponseEntity<UserDto> updateMyProfile(@RequestBody UserDto userDto) {
        try {
            // 현재 로그인된 사용자의 정보 불러오기
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            Optional<User> optionalUser = userRepository.findByEmail(email);

            // optionalUser가 비어있으면 404 에러 반환
            if (!optionalUser.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            // UserDto 객체를 User 객체로 변환하여 DB에 저장
            User user = optionalUser.get();
            user.setNickname(userDto.getNickname());
            user.setSemester(userDto.getSemester());
            user.setGraduate(userDto.isGraduate());
            user.setDepartment(userDto.isDepartment());
            user.setMajor_minor(userDto.isMajor_minor());
            user.setDouble_major(userDto.isDouble_major());
            user.setMajor1(userDto.getMajor1());
            user.setMajor2(userDto.getMajor2());
            userRepository.save(user);

            // User 객체를 UserDto로 변환하여 전송
            UserDto updatedUserDto = UserDto.convertToDto(Optional.ofNullable(user));
            return ResponseEntity.ok(updatedUserDto);
        } catch (Exception e) {
            // 예외 발생 시 500에러 응답 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}