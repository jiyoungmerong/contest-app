package com.example.contest_app.repository;

import com.example.contest_app.domain.Route;
import com.example.contest_app.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Integer>{

    List<Route> findAll();

    List<Route> findByUser(User user);

    List<Route> findByUserNickname(String userNickname);


    Page<Route> findAllByDepartmentContaining(String department, Pageable pageable);

    List<Route> findAllByDepartment(String department);


}
