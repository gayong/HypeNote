package com.gpt.gpt.controller;

import com.gpt.gpt.dto.GptClientRequestDto;
import com.gpt.gpt.service.GptApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gpt")
public class GptApiController {

    private final GptApiService gptApiService;

    @PostMapping("/chat")
    public String getChatGptResponse(@RequestBody GptClientRequestDto gptClientRequestDto) {
        return gptApiService.getChatGptResponse(gptClientRequestDto);
    }
}