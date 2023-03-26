package com.example.contest_app.repository;

import com.example.contest_app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByEmail(String email);

    void deleteById(int user_id);

    Optional<User> findByNickname(String nickname);

//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE User u SET u.nickname = :nickname, u.semester = :semester, u.graduate = :graduate, u.department = :department, u.major_minor = :major_minor, u.double_major = :double_major, u.major1 = :major1, u.major2 = :major2 WHERE u.email = :email")
//    void updateUser(@Param("nickname") String nickname,
//                    @Param("semester") int semester, @Param("graduate") boolean graduate,
//                    @Param("department") boolean department, @Param("major_minor") boolean major_minor,
//                    @Param("double_major") boolean double_major, @Param("major1") String major1,
//                    @Param("major2") String major2);

    User findByEmailAndPassword(String email, String password);



}