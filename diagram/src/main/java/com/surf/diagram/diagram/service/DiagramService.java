package com.surf.diagram.diagram.service;

import com.surf.diagram.diagram.dto.editor.EditorListResponseDto;
import com.surf.diagram.diagram.dto.response.DiagramResponseDto;
import com.surf.diagram.diagram.dto.response.LinkResponseDto;
import com.surf.diagram.diagram.dto.response.NodeResponseDto;
import com.surf.diagram.diagram.entity.Node;

import java.util.List;

public interface DiagramService {

//    DiagramResponseDto linkNodesByShare(int userId, int targetUserId);
//
    DiagramResponseDto linkNodesByShares(int userId, List<Integer> targetUserIds);
    DiagramResponseDto getDiagram(int userId);
}