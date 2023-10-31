package com.surf.quiz.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionDto {
    private int id;
    private String question;

    private List<String> example;

    private String answer;
}
