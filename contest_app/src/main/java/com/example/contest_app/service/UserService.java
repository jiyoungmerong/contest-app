package com.example.contest_app.service;
import com.example.contest_app.exception.InvalidPasswordException;
import com.example.contest_app.exception.UserNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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


    public void save(UserDto userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword()); // 패스워드 인코딩
        userDto.setPassword(encodedPassword);

        User user = new User(); 
        user.setEmail(userDto.getEmail());
        user.setPassword(encodedPassword); // 인코딩된 패스워드 저장
        user.setNickname(userDto.getNickname());
        user.setSemester(userDto.getSemester());
        user.setGraduate(userDto.isGraduate());
        user.setMajor1(userDto.getMajor1());
        user.setMajor2(userDto.getMajor2());
        user.setDepartment(userDto.isDepartment());
        user.setMajor_minor(userDto.isMajor_minor());
        user.setDouble_major(userDto.isDouble_major());

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


    public List<String> getRouteInfo(User user) {
        Optional<User> optionalUser = userRepository.findByNickname(user.getNickname());
        if (optionalUser.isPresent()) {
            User foundUser = optionalUser.get();
            return Collections.singletonList(foundUser.getRouteInfo());
        }
        return Collections.emptyList();
    }

    public boolean isCorrectPassword(int user_id, String password){ // 탈퇴시 비밀번호 확인
        User user = userRepository.findById(user_id).orElse(null);
        if(user!=null){
            String encodepassword = user.getPassword();
            return passwordEncoder.matches(password, encodepassword);
        }
        return false;
    }

    public void deleteUser(int user_id, String password) throws UserNotFoundException, InvalidPasswordException {
        Optional<User> userOptional = userRepository.findById(user_id);
        if(!userOptional.isPresent()){
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }
        User user = userOptional.get();
        if(!isCorrectPassword(user.getId(), password)){
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
        userRepository.delete(user);
    }



}