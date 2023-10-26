package com.surf.quiz.entity;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResult {

    private Long quizId;
    private Long userId;
    private Long totals;
    private Long Correct;
    private LocalDateTime examDone;

}
