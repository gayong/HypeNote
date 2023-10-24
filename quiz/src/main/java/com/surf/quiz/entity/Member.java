package com.surf.quiz.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Member {

    private Long userId;
    private String userName;

    private int userScore;
    private int userLanking;

    private int quizCount;
    private int correct;

    // True == 방장, False == 일반
    private boolean host;

    // True == 레디, False == 대기
    private boolean ready;
}
