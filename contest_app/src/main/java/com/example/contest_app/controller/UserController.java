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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final EncryptionService encryptionService;

    private final UserRepository userRepository;

    @PostMapping("/users/new-user") // 회원가입
    public ResponseEntity<String> join(@RequestBody UserDto userDto) {
        userService.save(userDto);
        return ResponseEntity.ok("Join success"); // 메시지 명시
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
    public ResponseEntity<String> deleteUser(@RequestBody DeleteRequest deleteRequest, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        int currentUserId = currentUser.getId();

        String password = deleteRequest.getPassword();
        if (password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호를 입력해주세요.");
        }

        try {
            String encryptedPassword = encryptionService.encrypt(password); // 비밀번호 암호화
            userService.deleteUser(currentUserId, encryptedPassword);
            return ResponseEntity.ok("사용자가 성공적으로 삭제되었습니다.");
        } catch (UserNotFoundException | InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (EncryptionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 암호화에 실패했습니다.");
        }
    }

    @PostMapping("/logout") // 로그아웃
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {
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
        LogoutResponse response = new LogoutResponse("로그아웃 되었습니다.", newSessionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-profile") // 사용자 정보 가져오기
    @ResponseBody
    public ResponseEntity<UserDto> getMyProfile() {
        try {
            // 현재 로그인된 사용자의 정보 불러오기
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            Optional<User> user = userRepository.findByEmail(email);

            // 사용자 정보를 UserDto로 변환하여 전송
            UserDto userDto = UserDto.convertToDto(user);
            return ResponseEntity.ok(userDto);
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
            UserDto updatedUserDto = UserDto.convertToDto(optionalUser);
            return ResponseEntity.ok(updatedUserDto);
        } catch (Exception e) {
            // 예외 발생 시 500에러 응답 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}