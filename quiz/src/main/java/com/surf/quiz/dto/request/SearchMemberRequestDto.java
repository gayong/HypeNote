package com.surf.quiz.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class SearchMemberRequestDto {
    String roomName;
    String content;
    int quizCnt;
    boolean single;
    List<Integer> pages;
    List<Integer> sharePages;
}
