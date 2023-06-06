package com.example.contest_app.domain.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {

    private String email;
    
    private String accessToken;

    public static LoginResponse of(String email, String accessToken){
        return new LoginResponse(email, accessToken);
    }


}
