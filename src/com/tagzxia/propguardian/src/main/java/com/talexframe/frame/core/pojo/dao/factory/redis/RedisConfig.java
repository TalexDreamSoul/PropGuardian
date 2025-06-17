package com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.redis;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * <br /> {@link com.talexframe.frame.config Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 15:48 <br /> Project: TalexFrame <br />
 */
@Configuration
@Getter
public class RedisConfig {

    @Getter
    private static RedisConfig instance;
    private RedisConnectionFactory factory;
    private RedisCacheManager redisCacheManager;
    private RedisTemplate<String, Object> redisTemplate;
    private RedisCacheConfiguration redisCacheConfiguration;
    public RedisConfig() {

        instance = this;

    }

    @Bean
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        this.factory = redisConnectionFactory;

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();

        this.redisTemplate = redisTemplate;

        return redisTemplate;

    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        redisCacheConfiguration.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));

        this.redisCacheConfiguration = redisCacheConfiguration;

        Map<String, RedisCacheConfiguration> redisExpireConfig = new HashMap<>();

        redisExpireConfig.put("1min", RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .entryTtl(Duration.ofMinutes(1)).disableCachingNullValues());

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(redisExpireConfig)
                .transactionAware()
                .build();

        this.redisCacheManager = redisCacheManager;

        return redisCacheManager;

    }

}
