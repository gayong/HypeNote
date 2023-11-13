package com.surf.diagram.diagram.service;


import com.surf.diagram.diagram.dto.response.DiagramResponseDto;
import java.util.List;

public interface DiagramService {
    DiagramResponseDto linkNodesByShares(int userId, List<Integer> targetUserIds);
    DiagramResponseDto getDiagram(int userId);
}