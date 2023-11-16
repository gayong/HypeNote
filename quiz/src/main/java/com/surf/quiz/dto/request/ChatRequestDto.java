package com.surf.quiz.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRequestDto {
    String userPk;
    String userName;
    String userImg;
    String content;
}
