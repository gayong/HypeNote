package com.surf.quiz.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuestionDto {
    private String question;

    ExampleDto example;

    private String answer;
}
