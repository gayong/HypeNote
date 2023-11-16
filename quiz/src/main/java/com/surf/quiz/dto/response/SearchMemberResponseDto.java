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
    List<String> pages;
    List<String> sharePages;
    private List<UserDto> inviteUsers;
}
