package hello.demo.service;

import hello.demo.domain.User;

public interface SignInService {

    User logIn(Long id, String pw);

}
