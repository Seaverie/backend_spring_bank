package com.banking.demo.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final StringRedisTemplate redisTemplate;

    public boolean isOperationProcessed(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void storeOperationKey(String key) {
        redisTemplate.opsForValue().set(key, "PROCESSED", Duration.ofHours(24));
    }
}
