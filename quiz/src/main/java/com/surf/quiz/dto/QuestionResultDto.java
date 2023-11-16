package com.surf.quiz.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionResultDto {
    private int id;
    private String question;

    private List<ExampleDto> example;

    private String answer;
    private String myAnswer;
    private String commentary;
}
