package com.example.contest_app.service;

import com.example.contest_app.domain.Lecture;
import com.example.contest_app.domain.Route;
import com.example.contest_app.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;

    public List<Route> findRoutesByDepartment(String department) {
        List<Lecture> lectures = lectureRepository.findByDepartment(department);
        List<Route> routes = new ArrayList<>();
        for(Lecture lecture : lectures) {
            routes.addAll(lecture.getRoute());
        }
        return routes;
    }
}
