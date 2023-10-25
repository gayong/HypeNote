package com.surf.quiz.entity;


import com.surf.quiz.dto.ExampleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Quiz {
    String quizId;
    @CreatedDate
    private LocalDateTime createdDate;
    private String question;
    ExampleDto example;
    private String answer;
    private boolean complete;
}
