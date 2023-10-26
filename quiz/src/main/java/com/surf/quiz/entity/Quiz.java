package com.surf.quiz.entity;

import com.surf.quiz.dto.QuestionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {
    @Indexed(unique = true)
    String quizId;
    int roomId;
    @CreatedDate
    private LocalDateTime createdDate;

    List<QuestionDto> question;

    Map<String, List<String>> userAnswers = new HashMap<>();

    private boolean complete;
}
