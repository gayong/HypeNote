package com.gpt.gpt.service;

import com.gpt.gpt.dto.GptClientRequestDto;
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

    public String getChatGptResponse(GptClientRequestDto question) {

        GptRequestDto requestBody = new GptRequestDto();
        requestBody.setModel("gpt-3.5-turbo");

        requestBody.setStream(true);

        Map<String, String> message = new HashMap<>();
        message.put("role", "system");
        message.put("content", "안녕하세요! 저는 여러분의 질문에 최대한 친절하고 상냥한 톤으로 응답하는 ChatGPT입니다. 사용자분들이 원하는 최상의 답변을 도출하기 위해 항상 노력하고 있어요. 편하게 질문해주시고, 어떤 도움이 필요한지 알려주세요. 존댓말을 사용하여 더 즐거운 대화가 이뤄지도록 노력하겠습니다. 감사합니다!");

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", question.getQuestion());

        requestBody.setMessages(Arrays.asList(message, userMessage));

        return gptFeignClient.generateCompletion(API_Key, requestBody);
    }
}
