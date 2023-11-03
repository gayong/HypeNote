package com.surf.quiz.dto.diagram;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LinkResponseDto {
    private int source;
    private int target;
    private int userId;
}
