package hello.demo.repository;

import hello.demo.domain.User;

import java.util.List;

public interface UserRepository {
    void save(User user); //회원생성
    User findById(Long id);
    User findByName(String name);
}
