package com.surf.editor.websocket.controller;

import com.surf.editor.websocket.dto.WebSocketRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/note/{noteId}")
    public WebSocketRequestDto handleChat(@DestinationVariable("noteId") Long noteId, WebSocketRequestDto jsonDataList) {
        //DestinationVariable 는 PathVariable 과 유사
//        log.info("Received message: {}, {}", jsonDataList.getContent(),jsonDataList.getType());
        simpMessagingTemplate.convertAndSend("/sub/note/"+ noteId, jsonDataList);
        return jsonDataList;
    }

}
