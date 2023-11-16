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
        if (body.getContent() == null || body.getContent().trim().isEmpty()) {
            return null;
        }
        ChatResponseDto chatDto = new ChatResponseDto();
        chatDto.setContent(body.getContent());
        chatDto.setUserPk(Integer.parseInt(body.getUserPk()));
        chatDto.setUserName(body.getUserName());
        chatDto.setUserImg(body.getUserImg());
        if (body.getUserImg().equals("성공")) {
            chatDto.setUserImg("/assets/유령.png");
        }
        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        chatDto.setChatTime(formattedDateTime);
        return chatDto;
    }

    public void sendStompMessage(int roomId, ChatResponseDto body) {
        String destination = String.format("/sub/chat/%d", roomId);
        if (body==null){
            return;
        }
        messageTemplate.convertAndSend(destination, body);
    }


}
