package com.surf.quiz.dto.response;


import com.surf.quiz.dto.ResultDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuizResultResponseDto {
    private Long quizCnt;
    private Long totalCnt;
    private Long correctCnt;
    private List<ResultDto> quiz;
}
