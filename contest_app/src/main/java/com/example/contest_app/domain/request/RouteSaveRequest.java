package com.example.contest_app.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RouteSaveRequest {
    private String title;

    private String department;

    private String recommendation; // 루트 추천

    private List<RouteInfoRequest> routeInfos; // routeInfo 불러오기

}
