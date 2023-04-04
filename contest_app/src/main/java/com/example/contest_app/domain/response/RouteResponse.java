package com.example.contest_app.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {
    private Long id;
    private String title;
    private String department;
    private List<String> routeInfoTitles;


}