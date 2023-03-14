package com.example.contest_app.repository;

import com.example.contest_app.domain.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer>{
    void delete(Optional<Evaluation> evaluation);
}
