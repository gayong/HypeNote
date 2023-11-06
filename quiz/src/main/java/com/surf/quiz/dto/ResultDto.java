package com.surf.quiz.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultDto {
    private int quizId;
    private String roomName;
    private int totals;
    private String examDone;
    private int correct;
}
