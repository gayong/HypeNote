package com.surf.quiz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {

    private Long userPk;
    private String userName;
    private String userImg;

    // True == 방장, False == 일반
    private boolean host;

    // True == 레디, False == 대기
    private boolean ready;
}