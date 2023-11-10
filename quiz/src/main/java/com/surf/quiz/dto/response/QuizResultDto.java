package com.surf.quiz.dto.response;

import com.surf.quiz.dto.MemberDto;
import com.surf.quiz.dto.QuestionResultDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuizResultDto {

    private Long quizId;
    private int roomId;
    private String roomName;
    private int totals;
    private int Correct;
    private String examStart;
    private String examDone;
    List<QuestionResultDto> questionResult;
}
