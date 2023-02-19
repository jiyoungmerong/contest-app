package hello.demo.service;

import hello.demo.domain.User;

public interface SignUpService {

    void join(User user);

    User findUser(Long id);

}
