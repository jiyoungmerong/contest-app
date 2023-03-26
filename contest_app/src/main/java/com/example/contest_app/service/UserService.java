package com.example.contest_app.service;
import com.example.contest_app.domain.Route;
import com.example.contest_app.exception.InvalidPasswordException;
import com.example.contest_app.exception.UserNotFoundException;
import com.example.contest_app.repository.EvaluationRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.UserDto;
import com.example.contest_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EvaluationRepository evaluationRepository;
    // private String authKey;

//    public void save(UserDto userDto){
//        userDto.setPassword(passwordEncoder.encode(userDto.getPassword());
//        userRepository.save(userDto.toEntity());
//    }

    public void save(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(userDto.toEntity());
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




    public String login(String email, String password) {

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())){
            return "true";
        }
        return "false";

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