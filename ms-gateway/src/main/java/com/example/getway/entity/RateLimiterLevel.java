package com.example.getway.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @author czx
 * @title: RateLimiterConfig
 * @projectName demo1
 * @description: TODO 限流等级配置
 * @date 2019/7/515:26
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rate-limiter-levels")
public class RateLimiterLevel {

    private Map<String,Integer[]> levels;
}
