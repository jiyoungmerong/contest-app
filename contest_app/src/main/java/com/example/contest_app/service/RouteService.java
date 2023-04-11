package com.example.contest_app.service;

import com.example.contest_app.domain.Route;
import com.example.contest_app.domain.User;
import com.example.contest_app.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;


    public void save(Route route){
        routeRepository.save(route);
    }

    public Page<Route> findByDepartment(String department, Pageable pageable) {
        return routeRepository.findAllByDepartmentContaining(department, pageable);
    }

    public List<Route> findByUser(User user) {
        List<Route> routes = routeRepository.findByUser(user);
        return routes == null ? Collections.emptyList() : routes;
    }





}
