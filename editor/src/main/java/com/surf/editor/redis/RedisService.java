package com.surf.editor.redis;


import com.surf.editor.websocket.dto.EditorConnectionRequestDto;
import com.surf.editor.websocket.dto.EditorDisconnectionRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RedisService {

    private final EditorRedisRepository editorRedisRepository;

    public List<Integer> addKey(String key, EditorConnectionRequestDto editorConnectionRequestDto){
        RedisInfo redisInfo = editorRedisRepository.findById(key).orElse(null);

        if(redisInfo==null){
            RedisInfo redis = new RedisInfo(key, new ArrayList<>());
            redis.addValue(editorConnectionRequestDto.getUserId());

            RedisInfo saved = editorRedisRepository.save(redis);

            return saved.getValue();
        }

        redisInfo.addValue(editorConnectionRequestDto.getUserId());
        RedisInfo saved = editorRedisRepository.save(redisInfo);

        return saved.getValue();
    }

    public List<Integer> subKey(String key, EditorDisconnectionRequestDto editorDisConnectionRequestDto) {
        RedisInfo redisInfo = editorRedisRepository.findById(key).orElse(null);

        if(redisInfo==null){
            return new ArrayList<>();
        }

        redisInfo.subValue(editorDisConnectionRequestDto.getUserId());
        RedisInfo saved = editorRedisRepository.save(redisInfo);

        return saved.getValue();
    }
}
