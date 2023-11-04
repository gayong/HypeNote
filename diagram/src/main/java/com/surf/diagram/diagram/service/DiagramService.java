package com.surf.diagram.diagram.service;

import com.surf.diagram.diagram.dto.response.DiagramResponseDto;

public interface DiagramService {
    void classifyAndSaveEmptyCategoryNodes(int userId) throws Exception;
    void linkNodesByCategoryAndConfidence(int userId);
    DiagramResponseDto linkNodesByShare(int userId, int targetUserId);
}
