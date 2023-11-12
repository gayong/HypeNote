package com.gpt.gpt.service;

import com.gpt.gpt.dto.GptClientRequestDto;
import com.gpt.gpt.dto.GptRequestDto;
//import com.gpt.gpt.feign.GptFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GptApiService {

//    private final GptFeignClient gptFeignClient;
    private final WebClient client = WebClient.create("https://api.openai.com/v1/");

    @Value("${apikey}")
    private String API_Key;

    public Flux<String> getChatGptResponse(GptClientRequestDto question) {

        GptRequestDto requestBody = new GptRequestDto();
        requestBody.setModel("gpt-3.5-turbo");

        requestBody.setStream(true);

        Map<String, String> message = new HashMap<>();
        message.put("role", "system");
        message.put("content", "안녕하세요! 저는 여러분의 질문에 최대한 친절하고 상냥한 톤으로 응답하는 ChatGPT입니다.");

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", question.getQuestion());

        requestBody.setMessages(Arrays.asList(message, userMessage));

        return client.post()
                .uri("/chat/completions")
                .header("Authorization", API_Key)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class);
    }
}