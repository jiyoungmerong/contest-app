package com.example.contest_app.repository;

import com.example.contest_app.domain.Evaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer>{

    Page<Evaluation> findByLecture_Department(String department, Pageable pageable);

    Page<Evaluation> findAllByLectureIdIn(List<Integer> lectureIds, Pageable pageable);
    Page<Evaluation> findByLecture_DepartmentContaining(String department, Pageable pageable);

}
