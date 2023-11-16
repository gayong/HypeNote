package com.surf.quiz.dto.response;

import com.surf.quiz.dto.MemberDto;
import com.surf.quiz.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//@NoArgsConstructor
@Builder
public class DetailResponseDto {
    private Long id;
    private String roomName;
    private String content;
    private Long host;

    private int roomMax;
    private int roomCnt;

    private int readyCnt;
    private int quizCnt;

    // false == 대기, true == 실행
    private boolean roomStatus;


    private String createdDate;

    private List<String> pages;
    private List<String> sharePages;

    private boolean single;

    private List<UserDto> inviteUsers;
    private List<MemberDto> users;
}
