package com.example.contest_app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "evaluaion")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private int id;

    private String lectureName; // 강의 이름

    private String prfsName; // 교수님 이름

    private int classYear; // 수강년도

    private int semester; // 수강 학기

    private String department;

    private int teamPlay; // 팀플정도

    private int task; // 과제정도

    private int practice; // 실습정도

    private int presentation; // 발표 정도

    private String userNickname; // 유저 닉네임

    @Column(length = 150)
    private String review; // 총평

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("evaluations")
    private User user;

    // User 필드에 대한 getter
    @JsonProperty("user")
    public User getUser() {
        return user;
    }

    // User 필드에 대한 setter
    @JsonProperty("user")
    public void setUser(User user) {
        this.user = user;
    }
}