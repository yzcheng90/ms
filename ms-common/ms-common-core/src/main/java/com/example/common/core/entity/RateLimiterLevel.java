package com.example.common.core.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.Map;

/**
 * @author czx
 * @title: RateLimiterConfig
 * @projectName demo1
 * @description: TODO 限流等级配置
 * @date 2019/7/515:26
 */
@Data
public class RateLimiterLevel implements Serializable {
    private Map<String,Integer[]> levels;
}
