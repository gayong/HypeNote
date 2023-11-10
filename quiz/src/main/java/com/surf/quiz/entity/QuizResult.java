package com.surf.quiz.entity;

import com.surf.quiz.dto.MemberDto;
import com.surf.quiz.dto.QuestionResultDto;
import lombok.*;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class QuizResult {

    private Long quizId;
    private int roomId;
    private String roomName;
    private MemberDto user;
    private int totals;
    private int Correct;
    private String examStart;
    private String examDone;
    private LocalDateTime latestAnswerTime;
    List<QuestionResultDto> questionResult;
//    private List<String> pageLst;

}
