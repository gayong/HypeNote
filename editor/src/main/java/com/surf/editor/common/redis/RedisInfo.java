package com.surf.editor.common.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "redisInfo")
public class RedisInfo {

    @Id
    public String key;
    public List<Integer> value;

    public void addValue(Integer userId){
        value.add(userId);
    }

    public void subValue(Integer userId){
        value.remove(userId);
    }

}
