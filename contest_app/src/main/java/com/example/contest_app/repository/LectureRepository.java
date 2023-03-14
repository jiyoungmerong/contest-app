package com.example.contest_app.repository;

import com.example.contest_app.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    Lecture findByLectureAndPrfs(String lecture, String prfs);

}