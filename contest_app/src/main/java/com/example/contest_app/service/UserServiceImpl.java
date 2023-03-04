package com.example.contest_app.service;
import com.example.contest_app.domain.request.EmailRequest;
import org.springframework.stereotype.Service;

import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.UserDto;
import com.example.contest_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // private String authKey;

    @Override
    public void save(UserDto userDto){
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
        userRepository.save(userDto.toEntity());
    }

//    @Override
//    public void save(String authNum){
//        userRepository.save(authNum);
//    }

    @Override
    public String login(String id, String password) {

        Optional<User> user = userRepository.findById(id);

        if(user.get().getPassword().equals(user.get().getPassword())){
            return "Success";
        }
        return "Failed";

    }

    @Override
    public void delete(String id) {
//        User user = userRepository.findById(userDto.getId()).orElseThrow(()
//                -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
//        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
//            userRepository.deleteById(userDto.getId());
//            SecurityContextHolder.clearContext();
//        } else {
//            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
//        }
        userRepository.deleteById(id);
    }
//
//    @Override
//    public void update(String authKey) {
//        this.authKey = authKey;
//    }

    @Override
    public boolean checkNicknameDuplicate(String nickname){
        return userRepository.existsByNickname(nickname);
    }

}