package com.example.contest_app.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditRequest { // 회원정보 수정 요청
    private String nickname;

    private String semester;

    private boolean graduate;

    private boolean department; // 학부

    private boolean major_minor; // 주 부 전공

    private boolean double_major; // 복수전공

    private String major1;

    private String major2;
}
