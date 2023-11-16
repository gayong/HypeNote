package com.surf.editor.websocket.controller;

import com.surf.editor.websocket.dto.EditorConnectionRequestDto;
import com.surf.editor.websocket.dto.EditorDisconnectionRequestDto;
import com.surf.editor.websocket.dto.WebSocketRequestDto;
import com.surf.editor.websocket.service.EditorConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final EditorConnection editorConnection;

    @MessageMapping("/note/{noteId}")
    public WebSocketRequestDto handleChat(@DestinationVariable("noteId") String noteId, WebSocketRequestDto jsonDataList) {
        //DestinationVariable 는 PathVariable 과 유사
//        log.info("Received message: {}, {}", jsonDataList.getContent(),jsonDataList.getType());
        simpMessagingTemplate.convertAndSend("/sub/note/"+ noteId, jsonDataList);
        return jsonDataList;
    }

    @MessageMapping("/note/connection/{noteId}")
    public EditorConnectionRequestDto editorConnection(@DestinationVariable("noteId") String noteId, EditorConnectionRequestDto editorConnectionRequestDto){
        List<Integer> userList = editorConnection.editorConnection(noteId,editorConnectionRequestDto);
        simpMessagingTemplate.convertAndSend("/sub/note/connection/"+noteId, userList);

        return editorConnectionRequestDto;
    }

    @MessageMapping("/note/disconnection/{noteId}")
    public EditorDisconnectionRequestDto editorDisconnection(@DestinationVariable("noteId") String noteId, EditorDisconnectionRequestDto editorDisConnectionRequestDto){
        List<Integer> userList = editorConnection.editorDisconnection(noteId,editorDisConnectionRequestDto);
        simpMessagingTemplate.convertAndSend("/sub/note/connection/"+noteId, userList);

        return editorDisConnectionRequestDto;
    }


}
