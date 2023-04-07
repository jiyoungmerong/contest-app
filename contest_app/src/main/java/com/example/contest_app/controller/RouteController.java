package com.example.contest_app.controller;

import com.example.contest_app.domain.Route;
import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.RouteDto;
import com.example.contest_app.domain.request.RouteInfoRequest;
import com.example.contest_app.repository.RouteRepository;
import com.example.contest_app.service.RouteService;
import com.example.contest_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RouteController {
    private final UserService userService;

    private final RouteService routeService;
    private final RouteRepository routeRepository;

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
        route.setUser(user);
        route.setUserNickname(user.getNickname()); // 사용자의 닉네임 저장
        route.setDate(LocalDateTime.now()); // 현재 시간 저장
        try {
            Route savedRoute = routeRepository.save(route);

            RouteDto responseDto = new RouteDto(savedRoute);
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

    @GetMapping("/user/route") // 유저가 작성한 루트글 불러오기
    public ResponseEntity<List<RouteDto>> getUserRoutes(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Route> userRoutes = routeRepository.findByUser(user);

        List<RouteDto> responseDtoList = userRoutes.stream()
                .map(RouteDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtoList);
    }


    @GetMapping("/Allroutes") // 모든 루트 가져오기
    public ResponseEntity<List<RouteDto>> findAllRoutes() {
        // Route 엔티티 조회
        List<Route> routes = routeRepository.findAll();

        // RouteDto 리스트로 변환하여 반환
        List<RouteDto> routeDtos = routes.stream().map(RouteDto::new).collect(Collectors.toList());

        return ResponseEntity.ok(routeDtos);
    }

// 유저가 작성한 루트평 글 가져오기~



    @GetMapping("/route/{department}") // 전공별로 루프평 가져오기
    public ResponseEntity<List<RouteDto>> getRoutesByDepartment(@PathVariable String department,
                                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size,
                                                                HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Route> routes = routeService.findByDepartment(department, pageable);
        List<RouteDto> routeDtos = routes.getContent().stream()
                .map(RouteDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(routeDtos);
    }



}
