package com.surf.quiz.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AnswerDto {
    private Map<Long, String> answers;

}