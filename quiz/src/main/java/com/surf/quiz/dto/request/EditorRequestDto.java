package com.surf.quiz.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditorRequestDto {
    private String editorId;
    private int userId;
    private String title;
    private String content;
}
