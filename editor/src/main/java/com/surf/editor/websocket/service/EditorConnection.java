package com.surf.editor.websocket.service;

import com.surf.editor.redis.RedisService;
import com.surf.editor.websocket.dto.EditorConnectionRequestDto;
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
        /**
         * redis에 noteId를 key값으로 저장
         * 1. key값이 없다면 처음이니까 userId를 값으로 저장
         * 2. key값이 있다면 이미 접근 중인 user가 있으므로 해당 key값에 userId만 추가
         */
        List<Integer> userList = redisService.addKey(noteId, editorConnectionRequestDto.getUserId());

        return userList;
    }
}
