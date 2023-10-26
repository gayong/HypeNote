package com.surf.quiz.entity;

import com.surf.quiz.dto.QuestionResultDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuizResult {

    private String quizId;
    private String roomId;
    private Long userPk;
    private int totals;
    private int Correct;
    private LocalDateTime examDone;
    List<QuestionResultDto> questionResult;

}
