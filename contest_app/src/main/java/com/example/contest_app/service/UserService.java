package com.example.contest_app.service;
import com.example.contest_app.domain.dto.TokenDto;
import com.example.contest_app.exception.UserNotFoundException;
import com.example.contest_app.jwt.TokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.UserDto;
import com.example.contest_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;



    public void save(UserDto userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword()); // 패스워드 인코딩

        User user = User.builder()
                .email(userDto.getEmail())
                .password(encodedPassword)
                .nickname(userDto.getNickname())
                .semester(userDto.getSemester())
                .graduate(userDto.isGraduate())
                .major1(userDto.getMajor1())
                .major2(userDto.getMajor2())
                .department(userDto.isDepartment())
                .major_minor(userDto.isMajor_minor())
                .double_major(userDto.isDouble_major())
                .build();

        userRepository.save(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다." + email));
    }

    public boolean checkDuplicateNickname(String username) {
        return userRepository.findByNickname(username).isPresent();
    }

    public boolean checkDuplicateEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }


    public List<String> getRouteInfo(User user) {
        Optional<User> optionalUser = userRepository.findByNickname(user.getNickname());
        if (optionalUser.isPresent()) {
            User foundUser = optionalUser.get();
            return Collections.singletonList(foundUser.getRouteInfo());
        }
        return Collections.emptyList();
    }

    public void deleteUser(int userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        userRepository.delete(user.get());
    }

    public TokenDto login(String email, String password) {
        // 1. ID/PW 를 기반으로 Authentication 객체 생성
        // authentication 객체는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        //2. 실제 검증(사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 JwtUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증된 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDTO = tokenProvider.generateToken(authenticationToken);

        // 4. 로그인 성공하면 토큰DTO에 제대로 들어감
        return tokenDTO;
    }
}