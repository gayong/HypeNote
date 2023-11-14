package com.surf.diagram.diagram.service;


import com.surf.diagram.diagram.dto.response.DiagramResponseDto;
import java.util.List;

public interface DiagramService {
    DiagramResponseDto linkNodesByShares(String token, List<Integer> targetUserIds);
    DiagramResponseDto getDiagram(String token);
    DiagramResponseDto getLinkDiagram(String token);
}