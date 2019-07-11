package com.example.common.gateway.config;

import com.example.common.gateway.serialization.JdkSerializationStrategy;
import com.example.common.gateway.serialization.RedisTokenStoreSerializationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author czx
 * @title: StandardStringSerializationConfig
 * @projectName ms
 * @description: TODO 配置token序列化
 * @date 2019/7/1115:46
 */
@Configuration
public class StandardStringSerializationConfig {

    @Bean
    public RedisTokenStoreSerializationStrategy redisTokenStoreSerializationStrategy(){
        return new JdkSerializationStrategy();
    }
}
