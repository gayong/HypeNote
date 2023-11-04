//package com.surf.quiz.service;
//
//
//import com.surf.quiz.fegin.ChatCompletionClient;
//import com.surf.quiz.dto.gpt.ChatRequest;
//import com.surf.quiz.dto.gpt.Message;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//
//@Service
//@RequiredArgsConstructor
//public class ChatCompletionService {
//    private final ChatCompletionClient chatCompletionClient;
//    private final static String ROLE_USER = "user";
//    private final static String MODEL = "gpt-3.5-turbo";
//
//    @Value("${apikey}")
//    private String apikey;
//
//    public String chatCompletions(final String question) {
//        Message message = Message.builder()
//                .role(ROLE_USER)
//                .content(question)
//                .build();
//        ChatRequest chatRequest = ChatRequest.builder()
//                .model(MODEL)
//                .messages(Collections.singletonList(message))
//                .build();
//
//        return chatCompletionClient
//                .chatCompletions(apikey, chatRequest)
//                .getChoices()
//                .stream()
//                .findFirst()
//                .orElseThrow()
//                .getMessage()
//                .getContent();
//
//    }
//}
