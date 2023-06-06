package com.example.contest_app.domain.dto;

import com.example.contest_app.domain.Route;
import com.example.contest_app.domain.User;
import lombok.*;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RouteDto {

    private int id;

    private String title;
    private String department;
    private LocalDateTime createdAt;

    @PrePersist
    public void setDate() {
        this.createdAt = LocalDateTime.now();
    }

    private String userNickname;

    private User user;

    private String routeInfo;
    private String recommendation;

    public RouteDto(Route route) {
        this.title = route.getTitle();
        this.department = route.getDepartment();
        this.createdAt = route.getCreatedAt();
        this.userNickname = route.getNickname();
        this.user = route.getUser();
        this.routeInfo = route.getRouteInfo();
        this.recommendation = route.getRecommendation();
    }

    public Route toEntity() {
        Route route = new Route();
        route.setTitle(title);
        route.setDepartment(department);
        route.setRecommendation(recommendation);
        route.setRouteInfo(routeInfo);
        route.setCreatedAt(LocalDateTime.now());
        return route;
    }
}
