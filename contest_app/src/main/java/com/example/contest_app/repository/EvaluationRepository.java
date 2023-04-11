package com.example.contest_app.repository;

import com.example.contest_app.domain.Evaluation;

import com.example.contest_app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer>{


    List<Evaluation> findByUser(User user);

    List<Evaluation> findByDepartment(String department);

    List<Evaluation> findByLectureName(String lectureName);

    List<Evaluation> findAllByNickname(String nickname);


}
