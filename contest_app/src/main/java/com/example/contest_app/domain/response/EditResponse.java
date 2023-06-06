package com.example.contest_app.domain.response;

import com.example.contest_app.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EditResponse {

    private String email;
    private String nickname;
    private String semester;
    private boolean graduate;
    private boolean department;
    private boolean majorMinor;
    private boolean doubleMajor;
    private String major1;
    private String major2;

    private boolean major_minor; // 주 부 전공

    private boolean double_major; // 복수전공

    private String message;

    public EditResponse(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.semester = user.getSemester();
        this.graduate = user.isGraduate();
        this.department = user.isDepartment();
        this.major1 = user.getMajor1();
        this.major2 = user.getMajor2();
        this.major_minor = user.isMajor_minor();
        this.double_major = user.isDouble_major();
    }

    public EditResponse(String message) {
        this.message = message;
    }

}
