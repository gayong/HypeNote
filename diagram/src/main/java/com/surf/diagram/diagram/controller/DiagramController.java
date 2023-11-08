package com.surf.diagram.diagram.controller;

import com.surf.diagram.diagram.common.BaseResponse;
import com.surf.diagram.diagram.dto.response.LinkResponseDto;
import com.surf.diagram.diagram.dto.response.NodeResponseDto;
import com.surf.diagram.diagram.dto.response.DiagramResponseDto;
import com.surf.diagram.diagram.entity.Link;
import com.surf.diagram.diagram.entity.Node;
import com.surf.diagram.diagram.repository.LinkRepository;
import com.surf.diagram.diagram.repository.NodeRepository;
import com.surf.diagram.diagram.service.DiagramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diagram")
@Tag(name = "다이어그램", description = "다이어그램")
public class DiagramController {

    private final DiagramService diagramService;
    private final NodeRepository nodeRepository;
    private final LinkRepository linkRepository;

    public DiagramController(DiagramService diagramService, NodeRepository nodeRepository ,LinkRepository linkRepository) {
        this.diagramService = diagramService;
        this.nodeRepository = nodeRepository;
        this.linkRepository = linkRepository;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "내 노드와 링크 조회")
    public BaseResponse<DiagramResponseDto> getNodes(@PathVariable int userId) {
        List<Node> nodes = nodeRepository.findByUserId(userId);
        List<Link> links = linkRepository.findByUserId(userId);

        List<NodeResponseDto> nodeResponseDtos = nodes.stream()
                .map(node -> new NodeResponseDto(node.getId(), node.getTitle(), node.getUserId(), node.getEditorId()))
                .collect(Collectors.toList());

        List<LinkResponseDto> linkResponseDtos = links.stream()
                .map(link -> new LinkResponseDto(link.getSource(), link.getTarget(),  Math.exp(link.getSimilarity()) * 1 + 1, link.getUserId()))
                .collect(Collectors.toList());

        DiagramResponseDto response = new DiagramResponseDto(nodeResponseDtos, linkResponseDtos);
        return new BaseResponse<>(response);
    }



    @PostMapping("/keyword/{userId}")
    @Operation(summary = "키워드 분석")
    public BaseResponse<String> classifyAndSaveEmptyCategoryDiagrams(@PathVariable int userId) throws Exception {
        diagramService.classifyAndSaveEmptyCategoryNodes(userId);
        return new BaseResponse<>("모든 Diagram들이 성공적으로 분석되고 업데이트되었습니다.");
    }

    @PostMapping("/link/{userId}")
    @Operation(summary = "링크 연결")
    public BaseResponse<String> linkNodesByCategoryAndConfidence(@PathVariable int userId) throws Exception {
        diagramService.linkNodesByCategoryAndConfidence(userId);
        return new BaseResponse<>("링크 생성 완료");
    }


    @PostMapping("/share/{userId}/{targetUserId}")
    @Operation(summary = "1인 쉐어")
    public BaseResponse<DiagramResponseDto> linkNodesByShare(@PathVariable int userId, @PathVariable int targetUserId) throws Exception {
        DiagramResponseDto responseDto = diagramService.linkNodesByShare(userId, targetUserId);
        return new BaseResponse<>(responseDto);
    }

    @PostMapping("/share/{userId}")
    @Operation(summary = "다인 쉐어")
    public BaseResponse<DiagramResponseDto> linkNodesByShares(@PathVariable int userId, @RequestBody List<Integer> targetIds ) throws Exception {
        DiagramResponseDto responseDto = diagramService.linkNodesByShares(userId, targetIds);
        return new BaseResponse<>(responseDto);
    }
}
