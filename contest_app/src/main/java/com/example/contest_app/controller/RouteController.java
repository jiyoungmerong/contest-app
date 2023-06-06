package com.example.contest_app.controller;

import com.example.contest_app.domain.Route;
import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.RouteDto;
import com.example.contest_app.domain.request.RouteEditRequest;
import com.example.contest_app.domain.request.RouteInfoRequest;
import com.example.contest_app.repository.RouteRepository;
import com.example.contest_app.service.UserService;
import lombok.RequiredArgsConstructor;

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
        route.setNickname(user.getNickname()); // 사용자의 닉네임 저장
        route.setCreatedAt(LocalDateTime.now()); // 현재 시간 저장

        try {
            Route savedRoute = routeRepository.save(route);

            RouteDto responseDto = new RouteDto(savedRoute);
            responseDto.setUserNickname(savedRoute.getNickname()); // 닉네임 추가
            responseDto.setId(savedRoute.getId()); // 루트 ID 추가
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

    @GetMapping("/detail/route/{id}") // 루트 ID로 불러오기
    public ResponseEntity<?> getRouteById(@PathVariable int id) {
        Optional<Route> optionalRoute = routeRepository.findById(id);
        if (optionalRoute.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Route not found.");
        }

        Route route = optionalRoute.get();
        RouteDto routeDto = new RouteDto(route);
        routeDto.setRouteInfo(route.getRouteInfo());
        routeDto.setUserNickname(route.getNickname());
        routeDto.setCreatedAt(route.getCreatedAt()); // 생성된 시간 추가
        routeDto.setId(route.getId()); // 루트 ID 추가

        return ResponseEntity.ok(routeDto);
    }

    @GetMapping("my-routes") // 내가 작성한 루트 보기
    public ResponseEntity<List<RouteDto>> getAllRoutes(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Route> routes = routeRepository.findAllByNickname(user.getNickname()); // 수정된 부분
        List<RouteDto> routeDtos = new ArrayList<>();
        for (Route route : routes) {
            RouteDto routeDto = new RouteDto();
            routeDto.setId(route.getId()); // id 추가
            routeDto.setTitle(route.getTitle());
            routeDto.setDepartment(route.getDepartment());
            routeDto.setCreatedAt(route.getCreatedAt());
            routeDto.setUserNickname(route.getNickname());
            routeDto.setRouteInfo(route.getRouteInfo());
            routeDto.setRecommendation(route.getRecommendation());
            routeDtos.add(routeDto);
        }
        return ResponseEntity.ok(routeDtos);
    }


    @GetMapping("/user/routes")
    public ResponseEntity<?> getUserRoutes(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to access this resource.");
        }

        List<Route> routes = routeRepository.findByRouteInfo(user.getRouteInfo());

        List<Map<String, Object>> responseList = new ArrayList<>();
        for (Route route : routes) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("title", route.getTitle());
            responseMap.put("routeInfo", route.getRouteInfo());
            responseList.add(responseMap);
        }

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/all-routes") // 모든 루트 불러오기
    public ResponseEntity<List<RouteDto>> findAllRoutes() {
        List<Route> routes = routeRepository.findAllOrderByCreateAtDesc();
        List<RouteDto> routeDtos = routes.stream().map(route -> {
            RouteDto dto = new RouteDto(route);
            dto.setUserNickname(route.getNickname());
            dto.setCreatedAt(route.getCreatedAt()); // 생성된 시간 추가
            dto.setId(route.getId()); // id 추가
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(routeDtos);
    }

    @PutMapping("routes/{id}") // 루트추천 글 수정
    public ResponseEntity<RouteDto> updateRoute(@PathVariable int id, @Valid @RequestBody RouteEditRequest editResponse, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Optional<Route> optionalRoute = routeRepository.findById(id);
        if (optionalRoute.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Route route = optionalRoute.get();

        // 사용자가 작성한 루트인 경우에만 수정 가능
        if (editResponse.getTitle() != null) {
            route.setTitle(editResponse.getTitle());
        }
        if (editResponse.getDepartment() != null) {
            route.setDepartment(editResponse.getDepartment());
        }
        if (editResponse.getRecommendation() != null) {
            route.setRecommendation(editResponse.getRecommendation());
        }
        route.setRouteInfo(user.getRouteInfo());
        routeRepository.save(route);
        return ResponseEntity.ok(new RouteDto(route));
    }


    @DeleteMapping("/routes/{id}") // 루트글 삭제
    public ResponseEntity<?> deleteRoute(@PathVariable("id") int id, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to access this resource.");
        }

        Optional<Route> optionalRoute = routeRepository.findById(id);

        if (optionalRoute.isPresent()) {
            Route route = optionalRoute.get();

            // 사용자가 작성한 루트인지 확인
            if (!route.getNickname().equals(user.getNickname())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this route.");
            }

            routeRepository.delete(route);
            return ResponseEntity.ok("Route deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Route not found.");
        }
    }

    @GetMapping("/route/title") // 제목으로 검색
    public ResponseEntity<List<Route>> getRoutesByTitle(@RequestParam String title) {
        List<Route> routes = routeRepository.findByTitleContaining(title);
        return ResponseEntity.ok(routes);
    }
    @GetMapping("/route/{department}") // 전공에 따른 루트평검색
    public ResponseEntity<List<RouteDto>> getRouteByDepartment(@PathVariable String department) {
        List<Route> routes = routeRepository.findAllByDepartment(department);
        List<RouteDto> routeDTOs = new ArrayList<>();
        for (Route route : routes) {
            RouteDto routeDto = new RouteDto();
            routeDto.setTitle(route.getTitle());
            routeDto.setDepartment(route.getDepartment());
            routeDto.setCreatedAt(route.getCreatedAt());
            routeDto.setUserNickname(route.getNickname());
            routeDto.setRouteInfo(route.getRouteInfo());
            routeDto.setRecommendation(route.getRecommendation());
            routeDTOs.add(routeDto);
        }
        return ResponseEntity.ok(routeDTOs);
    }



}
