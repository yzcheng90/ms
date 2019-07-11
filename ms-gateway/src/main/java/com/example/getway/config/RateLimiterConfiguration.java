package com.example.getway.config;

import com.example.common.core.entity.StoreUser;
import com.example.common.gateway.serialization.RedisTokenStoreSerializationStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String AUTHORIZATION = "Authorization";
    private static final String AUTH_USER = "auth_user:";
    private static final String DEFAULT_LEVEL = "1";
    private static final String PREFIX = " ";
    private final RedisTemplate redisTemplate;
    private final RedisTokenStoreSerializationStrategy redisTokenStoreSerializationStrategy;

    @Bean
    public KeyResolver principalNameKeyResolver(){
//        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
        return exchange -> {
            List<String> authorization = exchange.getRequest().getHeaders().get(AUTHORIZATION);
            if(authorization != null && authorization.size() != 0){
                String token = authorization.get(0);
                token = token.substring(token.indexOf(PREFIX) + 1,token.length());
                String key = AUTH_USER + token;
                byte[] principal = redisTemplate.getConnectionFactory().getConnection().get(redisTokenStoreSerializationStrategy.serialize(key));
                if(principal != null){
                    StoreUser principalStr = redisTokenStoreSerializationStrategy.deserialize(principal,StoreUser.class);
                    log.error("-----------------------"+principalStr);
                    return Mono.just(DEFAULT_LEVEL);
                }
            }
            return Mono.just(DEFAULT_LEVEL);
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
