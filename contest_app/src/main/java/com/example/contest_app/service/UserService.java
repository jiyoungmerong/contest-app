package com.example.contest_app.service;
import com.example.contest_app.domain.Evaluation;
import com.example.contest_app.exception.InvalidPasswordException;
import com.example.contest_app.exception.UserNotFoundException;
import com.example.contest_app.repository.EvaluationRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.UserDto;
import com.example.contest_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public int save(UserDto userDto){
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(userDto.toEntity()).getUser_id();
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다." + email));
    }


//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
//        Optional<User> user = userRepository.findByEmail(email);
//        if(user == null){
//            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
//        }
//        return new Customer
//    }


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
        if(!isCorrectPassword(user.getUser_id(), password)){
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
        userRepository.delete(user);
    }

//    public Optional<User> authenticate(String email, String password){
//        Optional<User> user = userRepository.findByEmail(email);
//        if(user == null){
//            return null;
//        }
//        else
//            return user;
//    }

}