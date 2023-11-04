//package com.surf.quiz.fegin;
//
//
//import com.surf.quiz.dto.gpt.ChatRequest;
//import com.surf.quiz.dto.gpt.ChatResponse;
//import feign.Headers;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//
//@FeignClient(name="chat", url="https://api.openai.com/v1/")
//public class ChatCompletionClient {
//
//    @Headers("Content-Type: application/json")
//    @PostMapping("/chat/completios")
//    ChatResponse chatCompletions(@RequestHeader("Authorization") String apikey, ChatRequest request);
//}
