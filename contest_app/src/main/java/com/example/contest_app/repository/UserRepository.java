package com.example.contest_app.repository;

import com.example.contest_app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByEmail(String email);


    Optional<User> findByNickname(String nickname);

    User findByEmailAndPassword(String email, String password);

}