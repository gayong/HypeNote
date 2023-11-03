package com.surf.quiz.dto.diagram;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NodeResponseDto {
    private Long id;
    private String title;
    private int userId;      // group
    private int editorId;
}
