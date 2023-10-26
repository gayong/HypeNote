package com.surf.quiz.entity;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class QuizResult {

    private Long quizId;
    private Long userPk;
    private Long totals;
    private Long Correct;
    private LocalDateTime examDone;

}
