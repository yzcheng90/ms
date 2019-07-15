package com.example.common.gateway.config;

import com.example.common.gateway.handler.RedisLimiterLevelHandler;
import com.example.common.gateway.inteface.LimiterLevelResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author czx
 * @title: RedisLimiterConfig
 * @projectName ms
 * @description: TODO
 * @date 2019/7/1511:13
 */
@Configuration
public class RedisLimiterConfig {

    @Bean
    public LimiterLevelResolver limiterLevelResolver(){
        return new RedisLimiterLevelHandler();
    }
}
