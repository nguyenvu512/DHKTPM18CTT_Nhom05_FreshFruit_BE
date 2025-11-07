package com.example.fruitshop_be.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("redis-14487.c124.us-central1-1.gce.redns.redis-cloud.com");
        config.setPort(14487);
        config.setUsername("default");
        config.setPassword("Tqa3ZymGrjUOJ64hakaz4PCn95SYisdx");

        LettuceClientConfiguration clientConfig =
                LettuceClientConfiguration.builder()
                        .build();

        return new LettuceConnectionFactory(config, clientConfig);
    }
    // Template cho blacklist token
    @Bean
    public RedisTemplate<String, String> blacklistRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
