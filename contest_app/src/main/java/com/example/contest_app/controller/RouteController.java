package com.example.contest_app.controller;

import com.example.contest_app.domain.Route;
import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.RouteDto;
import com.example.contest_app.domain.request.RouteInfoRequest;
import com.example.contest_app.repository.RouteRepository;
import com.example.contest_app.service.RouteService;
import com.example.contest_app.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RouteController {
    private final UserService userService;

    private final RouteRepository routeRepository;

    private final EntityManager entityManager;

    @PostMapping("/save-route-info") // 선수과목제도 저장
    public ResponseEntity<String> saveRouteInfo(@RequestBody RouteInfoRequest request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to access this resource.");
        }

        try {
            user.setRouteInfo(request.getRouteInfo());
            userService.save(user);
            return ResponseEntity.ok("Route info saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving route info.");
        }
    }

    @PostMapping("/save/route") // 루트 저장 버튼
    public ResponseEntity<?> createRoute(@Valid @RequestBody RouteDto routeDto, BindingResult result, HttpSession httpSession) {
        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorMap);
        }

        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to access this resource.");
        }

        Route route = routeDto.toEntity();
        route.setRouteInfo(user.getRouteInfo()); // 사용자의 routeInfo 저장

        route.setUserNickname(user.getNickname()); // 사용자의 닉네임 저장
        route.setCreateAt(LocalDateTime.now()); // 현재 시간 저장
        try {
            Route savedRoute = routeRepository.save(route);

            RouteDto responseDto = new RouteDto(savedRoute);
            responseDto.setUserNickname(savedRoute.getUserNickname()); // 닉네임 추가
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving route.");
        }
    }


    @GetMapping("/user/routeInfo") //유저테이블에 있는 routeInfo 가져오기 (루트가져오기 버튼)
    public ResponseEntity<List<String>> getRecommendedRoutes(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userRouteInfo = user.getRouteInfo(); // user 테이블에 저장된 routeInfo 가져오기

        if (userRouteInfo == null || userRouteInfo.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<String> recommendedRoutes = Arrays.asList(userRouteInfo.split(","));

        return ResponseEntity.ok(recommendedRoutes);
    }

    @GetMapping("/user/routes") // 자신이 작성했던 루트
    public ResponseEntity<?> getRoutes(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to access this resource.");
        }
        List<Route> routes = routeRepository.findByUserNickname(user.getNickname());
        List<RouteDto> responseDtoList = new ArrayList<>();
        for (Route route : routes) {
            RouteDto responseDto = new RouteDto(route);
            responseDto.setUserNickname(route.getUserNickname()); // 닉네임 추가
            responseDtoList.add(responseDto);
        }
        return ResponseEntity.ok(responseDtoList);
    }


    @GetMapping("/Allroutes") // 모든 루트평
    public ResponseEntity<List<RouteDto>> findAllRoutes() {
        List<Route> routes = routeRepository.findAll();
        List<RouteDto> routeDtos = routes.stream().map(RouteDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(routeDtos);
    }

    @GetMapping("/route/{department}") // 전공에 따른 루트평
    public ResponseEntity<List<RouteDto>> getRouteByDepartment(@PathVariable String department) {
        List<Route> routes = routeRepository.findAllByDepartment(department);
        List<RouteDto> routeDTOs = new ArrayList<>();
        for (Route route : routes) {
            RouteDto routeDto = new RouteDto();
            routeDto.setTitle(route.getTitle());
            routeDto.setDepartment(route.getDepartment());
            routeDto.setCreateAt(route.getCreateAt());
            routeDto.setUserNickname(route.getUserNickname());
            routeDto.setRouteInfo(route.getRouteInfo());
            routeDto.setRecommendation(route.getRecommendation());
            routeDTOs.add(routeDto);
        }
        return ResponseEntity.ok(routeDTOs);
    }

}
