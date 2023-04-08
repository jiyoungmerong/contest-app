package com.example.contest_app.service;
import com.example.contest_app.exception.UserNotFoundException;
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

    public void deleteUser(int userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        userRepository.delete(user.get());
    }


}