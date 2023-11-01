package com.surf.diagram.diagram.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DiagramResponseDto {
    private List<NodeResponseDto> nodes;
    private List<LinkResponseDto> links;
}
