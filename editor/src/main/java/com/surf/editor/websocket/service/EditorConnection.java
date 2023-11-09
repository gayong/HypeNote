package com.surf.editor.websocket.service;

import com.surf.editor.common.redis.RedisService;
import com.surf.editor.websocket.dto.EditorConnectionRequestDto;
import com.surf.editor.websocket.dto.EditorDisconnectionRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EditorConnection {

    private final RedisService redisService;

    public List<Integer> editorConnection(String noteId, EditorConnectionRequestDto editorConnectionRequestDto) {
        List<Integer> userList = redisService.addKey(noteId, editorConnectionRequestDto);

        return userList;
    }

    public List<Integer> editorDisconnection(String noteId, EditorDisconnectionRequestDto editorDisConnectionRequestDto) {

        List<Integer> userList = redisService.subKey(noteId,editorDisConnectionRequestDto);

        return userList;
    }
}
