package com.surf.editor.common.redis;


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

    /**
     * 키와 유저 이이디가 들어옴
     * 1. 키가 없을 경우
     * 1-1. 새로 객체를 만들어준다.
     * 1-2. 값을 넣는다.
     * 1-3. 저장한다.
     * 2. 키가 있을 경우
     * 2-1. 해당 키에 값에 유저 아이디를 추가한다.
     * 2-2. 저장한다.
     */
    public List<Integer> addKey(String key, EditorConnectionRequestDto editorConnectionRequestDto){
        RedisInfo redisInfo = editorRedisRepository.findById(key).orElse(null);

        if(redisInfo==null){ //키가 없을 경우
            RedisInfo redis = new RedisInfo(key, new ArrayList<>());
            redis.addValue(editorConnectionRequestDto.getUserId());

            RedisInfo saved = editorRedisRepository.save(redis);

            return saved.getValue();
        }

        // 키가 있을 경우
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

        if (redisInfo.getValue().size()==0){
            editorRedisRepository.delete(redisInfo);
            return new ArrayList<>();
        }
        else{
            editorRedisRepository.save(redisInfo);
        }

        return redisInfo.getValue();
    }
}
