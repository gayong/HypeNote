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

    @GetMapping("/{userPk}")
    @Operation(summary = "내 노드와 링크 조회")
    public BaseResponse<DiagramResponseDto> getNodes(@PathVariable int userPk) {
        System.out.println("userId = " + userPk);

        DiagramResponseDto response = diagramService.getDiagram(userPk);

        return new BaseResponse<>(response);
    }


    @PostMapping("/share/{userPk}")
    @Operation(summary = "다인 쉐어")
    public BaseResponse<DiagramResponseDto> linkNodesByShares(@PathVariable int userPk, @RequestBody List<Integer> targetIds ) throws Exception {
        DiagramResponseDto responseDto = diagramService.linkNodesByShares(userPk, targetIds);
        return new BaseResponse<>(responseDto);
    }
}
