package com.surf.diagram.google.controller;

import com.surf.diagram.diagram.common.BaseResponse;
import com.surf.diagram.diagram.dto.response.DiagramResponseDto;
import com.surf.diagram.diagram.service.DiagramServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diagram/google")
@Tag(name = "구글", description = "구글")
public class GoogleController {

//    @Autowired
//    private DiagramServiceImpl diagramService;
//
//    @PostMapping("/keyword/{userId}")
//    @Operation(summary = "키워드 분석")
//    public BaseResponse<String> classifyAndSaveEmptyCategoryDiagrams(@PathVariable int userId) throws Exception {
//        diagramService.classifyAndSaveEmptyCategoryNodes(userId);
//        return new BaseResponse<>("모든 Diagram들이 성공적으로 분석되고 업데이트되었습니다.");
//    }
//
//    @PostMapping("/link/{userId}")
//    @Operation(summary = "링크 연결")
//    public BaseResponse<String> linkNodesByCategoryAndConfidence(@PathVariable int userId) throws Exception {
//        diagramService.linkNodesByCategoryAndConfidence(userId);
//        return new BaseResponse<>("링크 생성 완료");
//    }
//
//
//    @PostMapping("/share/{userId}/{targetUserId}")
//    @Operation(summary = "쉐어 노드")
//    public BaseResponse<DiagramResponseDto> linkNodesByShare(@PathVariable int userId, @PathVariable int targetUserId) throws Exception {
//        DiagramResponseDto responseDto = diagramService.linkNodesByShare(userId, targetUserId);
//        return new BaseResponse<>(responseDto);
//    }
}
