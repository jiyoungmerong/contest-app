package com.example.contest_app.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteRequest { // 회원탈퇴 요청
    private String password;
}