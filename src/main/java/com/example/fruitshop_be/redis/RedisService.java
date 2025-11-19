package com.example.fruitshop_be.redis;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class RedisService {
    RedisTemplate<String,String> blacklistRedisTemplate;
    static final String PREFIX = "blacklist:";
    static final long BLACKLIST_TTL_MILLIS = 3600_000; // 1 giờ

    public void blacklistToken(String jti) {
        String key = PREFIX + jti;
        blacklistRedisTemplate.opsForValue().set(key, "true", BLACKLIST_TTL_MILLIS, TimeUnit.MILLISECONDS);
    }
    public boolean isBlacklisted(String jti) {
        String key = PREFIX + jti;
        return Boolean.TRUE.equals(blacklistRedisTemplate.hasKey(key));
    }

}
