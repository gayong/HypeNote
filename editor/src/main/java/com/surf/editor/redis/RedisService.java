package com.surf.editor.redis;


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

    public List<Integer> addKey(String key, int value){
        RedisInfo redisInfo = editorRedisRepository.findById(key).orElse(null);

        if(redisInfo==null){
            List<Integer> userList = new ArrayList<>();
            userList.add(value);
            editorRedisRepository.save(new RedisInfo(key, userList));

            return userList;
        }

        List<Integer> redisInfoValue = redisInfo.getValue();
        redisInfoValue.add(value);
        editorRedisRepository.save(redisInfo);

        return redisInfoValue;

    }
}
