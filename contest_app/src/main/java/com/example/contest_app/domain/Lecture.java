package com.example.contest_app.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="Lecture")
@AllArgsConstructor
@Builder
public class Lecture { // 강의
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private int id;

    @Column(name = "lecture_name")
    private String name; // 강의 명

    private String prfs; // 교수님 이름

    @Column(name = "lecture_name" + "_" + "prfs_name", insertable = false, updatable = false)
    private String lecture;

    private boolean isMajorRequired; // 전공 필수인지 선택인지 필수면 true, 선택이면 false

    private String department; // 전공 구분

    @OneToMany(mappedBy = "lecture", fetch = FetchType.LAZY)
    private List<Evaluation> evaluations;

    @OneToMany(mappedBy = "lecture", fetch = FetchType.LAZY)
    private List<Route> route;

    @Builder
    public Lecture(String name, String prfs) {
        this.name = name;
        this.prfs = prfs;
    }

}