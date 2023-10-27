package com.surf.quiz.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatResponseDto {
    String chatTime;
    String userPk;
    String content;
}
