package com.trungnguyen.linkshortener.cache.implement;


import com.trungnguyen.linkshortener.cache.CacheInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisComponent<T> implements CacheInterface<T> {

    @Autowired
    private RedisTemplate redisTemplate;

    public static final long DEFAULT_EXPIRED_TIME = 60 * 60 * 100L;

    @Override
    public T getKey(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean addCache(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value, DEFAULT_EXPIRED_TIME, TimeUnit.MILLISECONDS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
