package com.surf.diagram.diagram.service;

import com.surf.diagram.diagram.dto.request.CreateDiagramDto;
import com.surf.diagram.diagram.dto.request.CreateDiagramWithParentDto;
import com.surf.diagram.diagram.dto.request.UpdateDiagramDto;
import com.surf.diagram.diagram.dto.request.UpdatePositionDto;
import com.surf.diagram.diagram.dto.response.DiagramResponseDto;
import com.surf.diagram.diagram.entity.Diagram;

import java.util.List;
import java.util.Optional;

public interface DiagramService {
    void classifyAndSaveEmptyCategoryNodes(int userId) throws Exception;
    void linkNodesByCategoryAndConfidence(int userId);
    DiagramResponseDto linkNodesByShare(int userId, int targetUserId);
}
