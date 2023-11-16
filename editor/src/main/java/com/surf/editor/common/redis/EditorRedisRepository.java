package com.surf.editor.common.redis;

import org.springframework.data.repository.CrudRepository;

public interface EditorRedisRepository extends CrudRepository<RedisInfo, String> {
}
