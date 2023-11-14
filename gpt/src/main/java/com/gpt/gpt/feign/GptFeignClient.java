package com.gpt.gpt.feign;

import com.gpt.gpt.dto.GptRequestDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="chat", url="https://api.openai.com/v1/")
public interface GptFeignClient {

    @Headers("Content-Type: application/json")
    @PostMapping("/chat/completions")
    String generateCompletion(@RequestHeader("Authorization") String API_Key, @RequestBody GptRequestDto requestBody);
}
