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
    List<String> pages;
    List<String> sharePages;
}
