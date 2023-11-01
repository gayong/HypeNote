package com.surf.diagram.google.controller;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v2.*;
import com.surf.diagram.diagram.common.BaseResponse;
import com.surf.diagram.diagram.dto.response.ClassificationResult;
import com.surf.diagram.diagram.service.DiagramServiceImpl;
import com.surf.diagram.google.dto.KeywordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diagram/google")
@Tag(name = "구글", description = "구글")
public class GoogleController {

    @Autowired
    private DiagramServiceImpl diagramService;

    @PostMapping("/keyword")
    @Operation(summary = "키워드 분석")
    public BaseResponse<String> classifyAndSaveEmptyCategoryDiagrams() throws Exception {
        diagramService.classifyAndSaveEmptyCategoryNodes();
        return new BaseResponse<>("모든 Diagram들이 성공적으로 분석되고 업데이트되었습니다.");
    }

    @PostMapping("/link")
    @Operation(summary = "링크 연결")
    public BaseResponse<String> linkNodesByCategoryAndConfidence() throws Exception {
        diagramService.linkNodesByCategoryAndConfidence();
        return new BaseResponse<>("링크 생성 완료");
    }
}
