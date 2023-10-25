package com.surf.quiz.entity;

import com.surf.quiz.dto.ExampleDto;
import com.surf.quiz.dto.QuestionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {
    String quizId;
    int roomId;
    @CreatedDate
    private LocalDateTime createdDate;

    List<QuestionDto> question;

    private boolean complete;
}
