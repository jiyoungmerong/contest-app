package com.example.contest_app.domain.dto;

import com.example.contest_app.domain.Route;
import com.example.contest_app.domain.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class RouteDto {
    private String title;
    private String department;
    private LocalDateTime date;

    private String userNickname;
    private String routeInfo;
    private String recommendation;

    public RouteDto(Route route) {
        this.title = route.getTitle();
        this.department = route.getDepartment();
        this.date = route.getDate();
        this.routeInfo = route.getRouteInfo();
        this.recommendation = route.getRecommendation();
    }

    public Route toEntity() {
        Route route = new Route();
        route.setTitle(title);
        route.setDepartment(department);
        route.setRecommendation(recommendation);
        route.setRouteInfo(routeInfo);
        route.setDate(LocalDateTime.now());
        return route;
    }

}
