package com.surf.quiz.controller;


import com.surf.quiz.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@RestController
@RequiredArgsConstructor
public class QuizChatController {

    private final SimpMessagingTemplate messageTemplate;


    @MessageMapping("/chat/{roomId}")
    public void sendMsgToRoom(@DestinationVariable int roomId, @Payload ChatDto body) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm");
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDateTime = dateTime.format(formatter);

        body.setChatTime(formattedDateTime);

        messageTemplate.convertAndSend("/sub/chat/" + roomId, body);
    }
}
