package com.surf.quiz.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.surf.quiz.common.BaseResponse;
import com.surf.quiz.common.BaseResponseStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surf.quiz.dto.QuestionDto;
import com.surf.quiz.dto.editor.ApiResponse;
import com.surf.quiz.dto.editor.EditorCheckResponse;
import com.surf.quiz.dto.request.EditorRequestDto;
import com.surf.quiz.entity.Editor;
import com.surf.quiz.fegin.ChatCompletionClient;
import com.surf.quiz.fegin.EditorServiceFeignClient;
import com.surf.quiz.repository.EditorRepository;
import com.surf.quiz.service.ChatCompletionService;
import com.surf.quiz.service.FeignService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/quiz")
public class EditorController {
    private final FeignService feignService;

    public EditorController(FeignService feignService) {
        this.feignService = feignService;
    }

    @GetMapping("/editor/{userId}")
    public BaseResponse<ApiResponse<EditorCheckResponse>> getEditorInfo(@PathVariable int userId) {
        return feignService.getEditorInfo(userId);
    }

    @PostMapping("/gpt")
    public BaseResponse<List<QuestionDto>> getGpt(@RequestParam int cnt, @RequestBody String content) {
        return feignService.getGpt(cnt, content);
    }
}
