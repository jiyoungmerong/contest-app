package com.example.contest_app.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="Department")
@AllArgsConstructor
@Builder
public class Department { // 학과

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private int id;

    @Column(name = "department_name")
    private String name; // 소프, 컴공, 정통, 인공지능

    @OneToMany(mappedBy = "department")
    private List<Lecture> lectures;

}