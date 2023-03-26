package com.example.contest_app.service;

import com.example.contest_app.domain.Lecture;
import com.example.contest_app.domain.Route;
import com.example.contest_app.repository.LectureRepository;
import com.example.contest_app.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;

    private final LectureRepository lectureRepository;


    public void save(Route route){
        routeRepository.save(route);
    }

    public Page<Route> findByDepartment(String department, Pageable pageable) {
        return routeRepository.findAllByDepartmentContaining(department, pageable);
    }


}
