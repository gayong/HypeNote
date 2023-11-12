package com.gpt.gpt.service;

import com.gpt.gpt.dto.GptRequestDto;
import com.gpt.gpt.feign.GptFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GptApiService {

    private final GptFeignClient gptFeignClient;

    @Value("${apikey}")
    private String API_Key;

    public String getChatGptResponse(String prompt) {

        GptRequestDto requestBody = new GptRequestDto();
        requestBody.setModel("gpt-3.5-turbo");

        requestBody.setStream(true);

        Map<String, String> message = new HashMap<>();
        message.put("role", "system");
        message.put("content", "답변을 표로도 잘 정리해서 보여줘.");

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);

        requestBody.setMessages(Arrays.asList(message, userMessage));

        return gptFeignClient.generateCompletion(API_Key, requestBody);
    }
}
