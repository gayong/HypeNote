package com.surf.quiz.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatDto {
    String chatTime;
    String user;
    String content;
}
