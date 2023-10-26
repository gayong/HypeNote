package com.surf.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class SearchMemberDto {
    String roomName;
    int quizCnt;
    boolean single;
    List<Integer> pages;
    List<Integer> sharePages;

}
