package com.example.contest_app.domain.dto;

import com.example.contest_app.domain.Route;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class RouteDto {
    //private int id;
    private String title; // 제목
    private String department; // 전공
    private LocalDate date; // 저장 날짜
    private String recommendation; // 내용

    public RouteDto(String title, String department,LocalDate date, String recommendation) {
        this.title = title;
        this.department = department;
        this.date = date;
        this.recommendation = recommendation;
    }


    public Route toEntity(){
        return Route.builder()
                .title(title)
                .department(department)
                .date(date)
                .recommendation(recommendation)
                .build();
    }
}
