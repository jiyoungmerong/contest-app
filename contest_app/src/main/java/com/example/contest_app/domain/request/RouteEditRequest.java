package com.example.contest_app.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RouteEditRequest {
    private String title;

    private String department;

    private String routeInfo;

    private String recommendation;
}


