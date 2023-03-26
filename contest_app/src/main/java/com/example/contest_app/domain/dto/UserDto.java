package com.example.contest_app.domain.dto;

import com.example.contest_app.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    // 회원가입 요청 DTO

    private String email; // 이메일

    private String password; // 비밀번호

    private String nickname; // 닉네임

    private int semester;

    private boolean graduate; // 졸업 여부

    private String major1; // 전공 1

    private String major2; // 전공 2

    private boolean department; // 학부

    private boolean major_minor; // 주 부 전공

    private boolean double_major; // 복수전공

    private String routeInfo; // 경로 정보


    public User toEntity(){
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .semester(semester)
                .graduate(graduate)
                .major1(major1)
                .major2(major2)
                .department(department)
                .major_minor(major_minor)
                .double_major(double_major)
                .routeInfo(routeInfo) // routeInfo 값을 User 엔티티 객체에 설정
                .build();
    }

    public static UserDto convertToDto(Optional<User> user) {
        if(user.isPresent()){
            User u = user.get();
            UserDto userDto = new UserDto();
            userDto.setNickname(u.getNickname());
            userDto.setSemester(u.getSemester());
            userDto.setGraduate(u.isGraduate());
            userDto.setDepartment(u.isDepartment());
            userDto.setMajor_minor(u.isMajor_minor());
            userDto.setDouble_major(u.isDouble_major());
            userDto.setMajor1(u.getMajor1());
            userDto.setMajor2(u.getMajor2());
            return userDto;
        }

        return null;
    }


}