package com.surf.quiz.dto.response;

import com.surf.quiz.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchMemberResponseDto {
    String roomName;
    String content;
    int quizCnt;
    boolean single;
    List<Integer> pages;
    List<Integer> sharePages;
    private List<UserDto> inviteUsers;
}
