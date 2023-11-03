package com.surf.quiz.dto.diagram;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DiagramResponseDto {
    private List<NodeResponseDto> nodes;
    private List<LinkResponseDto> links;
}
