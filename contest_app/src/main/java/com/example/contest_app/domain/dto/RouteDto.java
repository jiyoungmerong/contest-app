package com.example.contest_app.domain.dto;

import com.example.contest_app.domain.Route;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class RouteDto {
    //private int id;
    private String title; // 제목
    private String department; // 전공
    private LocalDate date; // 저장 날짜

    private String userNickname; // 유저 닉네임

    private String routeInfo; // 루트정보
    private String recommendation; // 내용

    public RouteDto(Route route) {
        this.title = route.getTitle();
        this.department = route.getDepartment();
        this.date = route.getDate();
        this.recommendation = route.getRecommendation();
        this.routeInfo = route.getRouteInfo();
    }


    public Route toEntity() {
        Route route = new Route();
        route.setTitle(title);
        route.setDepartment(department);
        route.setRecommendation(recommendation);
        route.setRouteInfo(routeInfo);
        route.setDate(LocalDate.now());
        return route;
    }

}
