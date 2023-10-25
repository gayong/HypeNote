package com.surf.quiz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateRoomDto {
    String roomName;
//    int roomMax;
    int quizCount;
    boolean single;
    List<Integer> pages;
    List<Integer> sharePages;

}
