package com.surf.quiz.controller;

import com.surf.quiz.dto.request.ChatRequestDto;
import com.surf.quiz.service.QuizChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizChatController {

    private final QuizChatService chatService;

    @MessageMapping("/chat/{roomId}")
    public void sendMsgToRoom(@DestinationVariable int roomId, @Payload ChatRequestDto body) {
        chatService.sendStompMessage(roomId, chatService.convertAndSetChatTime(body));
    }
}
