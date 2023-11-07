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
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class EditorController {
    @Autowired
    private EditorServiceFeignClient editorServiceFeignClient;
    @Autowired
    private ChatCompletionClient chatCompletionClient;

    private final ChatCompletionService chatCompletionService;

    private final EditorRepository editorRepository;

    @GetMapping("/editor/{userId}")
    @Operation(summary = "에디터 받기")
    public BaseResponse<ApiResponse<EditorCheckResponse>> getEditorInfo(@PathVariable int userId) {
        ApiResponse<EditorCheckResponse> response = editorServiceFeignClient.getEditor("65426205cd1e39028569f167");
        System.out.println("response = " + response.getData().getId());
        Editor editor = new Editor();
        editor.setEditorId(response.getData().getId());
        editor.setUserPk(1);
        editor.setContent("content");
        editor.setTitle("title");
        editorRepository.save(editor);
        return new BaseResponse<>(response);
    }

    @PostMapping("/gpt")
    @Operation(summary = "gpt/{cnt}")
    public BaseResponse<List<QuestionDto>> getGpt(@RequestParam int cnt, @RequestBody String content) {
        List<String> abc = chatCompletionService.chatCompletions(cnt, content);
        System.out.println("abc = " + abc);

        // ObjectMapper 객체 생성
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // JSON 문자열을 객체로 담고 있는 리스트로 변환
            List<QuestionDto> questionList = new ArrayList<>();
            for (String json : abc) {
                QuestionDto questionDto = objectMapper.readValue(json, new TypeReference<>() {});
                questionList.add(questionDto);
            }

            // 변환된 리스트 사용
            for (QuestionDto question : questionList) {
                System.out.println(question.getQuestion());
                System.out.println(question.getAnswer());
            }
            return new BaseResponse<>(questionList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new BaseResponse<>(null);
        }
    }
}
