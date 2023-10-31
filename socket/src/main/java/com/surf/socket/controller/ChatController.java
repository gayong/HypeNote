package com.surf.socket.controller;
import com.surf.socket.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messageTemplate;

    @MessageMapping("/lobby/chat")
    public void sendMsgToLobby(
            @Payload ChatMessageDto body) {

        messageTemplate.convertAndSend("/sub/lobby/chat", body);
    }

    @MessageMapping("/room/chat/{roomId}")
    public void sendMsgToRoom(
            @DestinationVariable int roomId,
            @Payload ChatMessageDto body) {

        messageTemplate.convertAndSend("/sub/room/chat/" + roomId, body);
    }
}