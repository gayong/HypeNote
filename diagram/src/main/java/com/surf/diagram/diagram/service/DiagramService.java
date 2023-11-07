package com.surf.diagram.diagram.service;

import com.surf.diagram.diagram.dto.response.DiagramResponseDto;

import java.util.List;

public interface DiagramService {
    void classifyAndSaveEmptyCategoryNodes(int userId) throws Exception;
    void linkNodesByCategoryAndConfidence(int userId);
    DiagramResponseDto linkNodesByShare(int userId, int targetUserId);

    DiagramResponseDto linkNodesByShares(int userId, List<Integer> targetUserIds);
}
