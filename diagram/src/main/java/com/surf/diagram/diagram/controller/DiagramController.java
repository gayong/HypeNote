package com.surf.diagram.diagram.controller;

import com.surf.diagram.diagram.common.BaseResponse;
import com.surf.diagram.diagram.dto.response.DiagramResponseDto;
import com.surf.diagram.diagram.service.DiagramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/diagram")
@Tag(name = "다이어그램", description = "다이어그램")
public class DiagramController {

    private final DiagramService diagramService;

    public DiagramController(DiagramService diagramService) {
        this.diagramService = diagramService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "내 노드와 링크 조회")
    public BaseResponse<DiagramResponseDto> getNodes(@PathVariable int userId) {
        System.out.println("userId = " + userId);

        DiagramResponseDto response = diagramService.getDiagram(userId);

        return new BaseResponse<>(response);
    }


    @PostMapping("/share/{userId}")
    @Operation(summary = "다인 쉐어")
    public BaseResponse<DiagramResponseDto> linkNodesByShares(@PathVariable int userId, @RequestBody List<Integer> targetIds ) throws Exception {
        DiagramResponseDto responseDto = diagramService.linkNodesByShares(userId, targetIds);
        return new BaseResponse<>(responseDto);
    }
}
