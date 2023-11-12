package com.gpt.gpt.controller;

import com.gpt.gpt.service.GptApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gpt")
public class GptApiController {

    private final GptApiService gptApiService;

    @GetMapping("/chat")
    public String getChatGptResponse(@RequestParam String prompt) {
        return gptApiService.getChatGptResponse(prompt);
    }
}