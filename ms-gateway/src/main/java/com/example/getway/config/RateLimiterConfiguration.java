package com.example.getway.config;

import com.example.common.core.constants.CommonConstants;
import com.example.common.core.constants.SecurityConstants;
import com.example.common.core.entity.StoreUser;
import com.example.common.gateway.serialization.RedisTokenStoreSerializationStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author czx
 * @title: RateLimiteConfiguration
 * @projectName demo1
 * @description: TODO 限流配置
 * @date 2019/7/515:46
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class RateLimiterConfiguration {

    private final RedisTemplate redisTemplate;
    private final RedisTokenStoreSerializationStrategy redisTokenStoreSerializationStrategy;

    @Bean
    public KeyResolver principalNameKeyResolver(){
        return exchange -> {
            List<String> authorization = exchange.getRequest().getHeaders().get(CommonConstants.AUTHORIZATION);
            if(authorization != null && authorization.size() != 0){
                String token = authorization.get(0);
                token = token.substring(token.indexOf(CommonConstants.PREFIX) + 1,token.length());
                String key = SecurityConstants.MS_OAUTH_PREFIX + CommonConstants.AUTH_USER + token;
                byte[] principal = redisTemplate.getConnectionFactory().getConnection().get(redisTokenStoreSerializationStrategy.serialize(key));
                if(principal != null){
                    StoreUser principalStr = redisTokenStoreSerializationStrategy.deserialize(principal,StoreUser.class);
                    log.error("-----------------------"+principalStr);
                    return Mono.just(principalStr.getLimitLevel() == 0 ? CommonConstants.DEFAULT_LEVEL : String.valueOf(principalStr.getLimitLevel()));
                }
            }
            return Mono.just(CommonConstants.DEFAULT_LEVEL);
        };
    }

    @Bean
    @Primary
    public RateLimiter customRedisRateLimiter(
            ReactiveRedisTemplate<String, String> redisTemplate,
            @Qualifier(CustomRedisRateLimiter.REDIS_SCRIPT_NAME) RedisScript<List<Long>> script,
            Validator validator){
        return new CustomRedisRateLimiter(redisTemplate,script,validator);
    }
}
