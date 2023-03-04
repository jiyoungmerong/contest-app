package com.example.contest_app.controller;
import com.example.contest_app.domain.dto.UserDto;
import com.example.contest_app.domain.request.loginRequest;
import com.example.contest_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users/new-user") // 회원가입
    public ResponseEntity<String> join(@RequestBody UserDto userDto,
                                       @RequestParam(value="이메일정보 정보", required = true) String email) {
        if(!userDto.getPassword().equals(userDto.getCheckpassword())){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } // 비밀번호와 확인용 비밀번호가 맞지 않을 경우, 400 Bad 발생시킴
        userService.save(userDto);
        return ResponseEntity.ok("join success"); // 정상적으로 작동할 시 200번 ok 발생
    }

    @PostMapping("/users/login") // 로그인
    public ResponseEntity login(@RequestBody loginRequest request) {
        if (userService.login(request.getId(),
                request.getPassword()).equals("Success")) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }

//    @DeleteMapping("users/id") // 회원 탈퇴
//    public ResponseEntity<String> deleteuser(@PathVariable("id") String id) {
//        userService.delete(id);
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @GetMapping("/user-nicknames/{nickname}/exists")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@PathVariable String nickname){
        return ResponseEntity.ok(userService.checkNicknameDuplicate(nickname));
    }

}
