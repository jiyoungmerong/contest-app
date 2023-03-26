package com.example.contest_app.repository;

import com.example.contest_app.domain.Evaluation;
import com.example.contest_app.domain.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    Lecture findByLectureAndPrfsAndDepartmentAndIsMajorRequired(String lecture, String prfs, String department, boolean is_major_required);

    List<Lecture> findByNameContaining(String name);

    List<Lecture> findByDepartment(String department);



}
