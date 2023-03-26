package com.example.contest_app.controller;

import com.example.contest_app.domain.Route;
import com.example.contest_app.domain.User;
import com.example.contest_app.domain.dto.RouteDto;
import com.example.contest_app.repository.RouteRepository;

import com.example.contest_app.service.RouteService;
import com.example.contest_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RouteController {
    private final UserService userService;

    private final RouteService routeService;
    private final RouteRepository routeRepository;

        // Autowired 등 필요한 필드/생성자 생략

    @PostMapping("/routes") // 루트 저장. 미졸업자.
    public ResponseEntity<?> createRoute(@Valid @RequestBody RouteDto routeDto, BindingResult result, Authentication authentication) {
        if (result.hasErrors()) {
            // 입력값 검증 실패 시, 에러 메시지를 포함한 응답 반환
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorMap);
        }

        // 현재 로그인되어 있는 사용자의 정보를 가져오기
        User user = (User) authentication.getPrincipal();

        // RouteDto를 Route 엔티티로 변환
        Route route = routeDto.toEntity();
        route.setDate(LocalDate.now());
        route.setUser(user);

        // Route 엔티티 저장
        Route savedRoute = routeRepository.save(route);

        // 저장된 Route 엔티티를 RouteDto로 변환하여 반환
        RouteDto savedRouteDto = RouteDto.builder()
                .title(savedRoute.getTitle())
                .department(savedRoute.getDepartment())
                .date(savedRoute.getDate())
                .recommendation(savedRoute.getRecommendation())
                .build();

        return ResponseEntity.ok(savedRouteDto);
    }

    @PostMapping("/save-route-info") // 루트 저장버튼
    public ResponseEntity<String> saveRouteInfo(@RequestParam("routeInfo") String routeInfo, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();

            user.setRouteInfo(routeInfo);
            userService.save(user);

            return ResponseEntity.ok("Route info saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving route info.");
        }
    }

    @GetMapping("/user/routes") //유저테이블에 있는 routeInfo 가져오기 (루트가져오기 버튼)
    public ResponseEntity<List<String>> getRoutes(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<String> routeInfo = userService.getRouteInfo(user);
        return ResponseEntity.ok(routeInfo);
    }

    @GetMapping("/Allroutes") // 모든 루트추천 불러오기
    public ResponseEntity<List<RouteDto>> findAllRoutes() {
        // 모든 Route 엔티티 조회
        List<Route> routes = routeRepository.findAll();

        // RouteDto 리스트로 변환하여 반환
        List<RouteDto> routeDtos = routes.stream()
                .map(route -> new RouteDto(route.getTitle(), route.getDepartment(), route.getDate(), route.getRecommendation()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(routeDtos);
    }

    @GetMapping("/route/recommend") // 로그인 한 유저가 작성한 루트추천
    public ResponseEntity<List<RouteDto>> findAllRoutes(Authentication authentication) {
        // 현재 로그인되어 있는 사용자의 정보를 가져오기
        User user = (User) authentication.getPrincipal();

        // 현재 사용자가 작성한 Route 엔티티 조회
        List<Route> routes = routeRepository.findByUser(user);

        // RouteDto 리스트로 변환하여 반환
        List<RouteDto> routeDtos = routes.stream()
                .map(route -> new RouteDto(route.getTitle(), route.getDepartment(), route.getDate(), route.getRecommendation()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(routeDtos);
    }

    @GetMapping("/route/{department}") // 전공별 루트평 출력
    public ResponseEntity<Page<Route>> findRoutesByDepartment(@PathVariable String department,
                                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Route> routes = routeService.findByDepartment(department, pageable);
        return ResponseEntity.ok(routes);
    }



}
