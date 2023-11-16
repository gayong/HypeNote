package com.surf.quiz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long userPk;
    private String userName;
    private String userImg;
}