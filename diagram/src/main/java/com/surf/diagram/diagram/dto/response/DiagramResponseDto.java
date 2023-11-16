package com.surf.diagram.diagram.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DiagramResponseDto {
    private List<NodeResponseDto> nodes;
    private List<LinkResponseDto> links;
}
