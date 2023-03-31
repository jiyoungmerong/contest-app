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
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RouteController {
    private final UserService userService;

    private final RouteService routeService;
    private final RouteRepository routeRepository;

    @PostMapping("/save-route-info") // 사용자가 작성한 루트 저장
    public ResponseEntity<String> saveRouteInfo(@RequestBody RouteInfoRequest request, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();

            user.setRouteInfo(request.getRouteInfo());
            userService.save(user);

            return ResponseEntity.ok("Route info saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving route info.");
        }
    }

//    @PostMapping("/save/route") // 루트 저장 버튼
//    public ResponseEntity<?> createRoute(@Valid @RequestBody RouteDto routeDto, BindingResult result, Authentication authentication) {
//        if (result.hasErrors()) {
//            Map<String, String> errorMap = new HashMap<>();
//            for (FieldError error : result.getFieldErrors()) {
//                errorMap.put(error.getField(), error.getDefaultMessage());
//            }
//            return ResponseEntity.badRequest().body(errorMap);
//        }
//
//        User user = (User) authentication.getPrincipal();
//
//        Route route = routeDto.toEntity();
//        route.setUser(user);
//        Route savedRoute = routeRepository.save(route);
//
//        RouteDto responseDto = new RouteDto(savedRoute);
//        return ResponseEntity.ok(responseDto);
//    }

    @PostMapping("/user/route-details") // 유저가 작성한 루트 글 가져오기
    public ResponseEntity<RouteDto> saveRouteDetails(@Valid @RequestBody RouteDto routeDto, BindingResult result, Authentication authentication) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        User user = (User) authentication.getPrincipal();

        Route route = routeDto.toEntity();
        route.setUser(user);
        user.addRoute(route);

        userService.save(user);

        RouteDto savedRouteDto = new RouteDto(route);
        return ResponseEntity.ok(savedRouteDto);
    }

    @GetMapping("/user/routes") //유저테이블에 있는 routeInfo 가져오기 (루트가져오기 버튼)
    public ResponseEntity<List<String>> getRoutes(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Route> routes = user.getRoutes();
        List<String> routeInfo = routes.stream()
                .map(Route::getRouteInfo)
                .collect(Collectors.toList());
        return ResponseEntity.ok(routeInfo);
    }

    @GetMapping("/Allroutes")
    public ResponseEntity<Page<RouteDto>> findAllRoutes(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        // 페이지 요청 정보 생성
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        // Route 엔티티 페이지 조회
        Page<Route> routePage = routeRepository.findAll(pageable);

        // RouteDto 페이지로 변환하여 반환
        Page<RouteDto> routeDtoPage = routePage.map(RouteDto::new);

        return ResponseEntity.ok(routeDtoPage);
    }

    @GetMapping("/route/recommend") // 로그인 한 유저가 작성한 루트추천
    public ResponseEntity<List<RouteDto>> findAllRoutes(Authentication authentication) {
        // 현재 로그인되어 있는 사용자의 정보를 가져오기
        User user = (User) authentication.getPrincipal();

        // 현재 사용자가 작성한 Route 엔티티 조회
        List<Route> route = routeRepository.findByUser(user);

        // RouteDto 리스트로 변환하여 반환
        List<RouteDto> routeDto = route.stream()
                .map(RouteDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(routeDto);
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
