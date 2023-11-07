package com.surf.quiz.service;


import com.surf.quiz.dto.request.ChatRequestDto;
import com.surf.quiz.dto.response.ChatResponseDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class QuizChatService {
    private final SimpMessagingTemplate messageTemplate;
//    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm");

    public QuizChatService(SimpMessagingTemplate messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public ChatResponseDto convertAndSetChatTime(ChatRequestDto body) {
        ChatResponseDto chatDto = new ChatResponseDto();
        chatDto.setContent(body.getContent());
        chatDto.setUserPk(body.getUserPk());
        chatDto.setUserName(body.getUserName());
        chatDto.setUserImg(body.getUserImg());
        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        chatDto.setChatTime(formattedDateTime);
        return chatDto;
    }

    public void sendStompMessage(int roomId, ChatResponseDto body) {
        String destination = String.format("/sub/chat/%d", roomId);
        messageTemplate.convertAndSend(destination, body);
    }


}
