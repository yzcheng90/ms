package com.example.admin.config;

import com.example.admin.entity.SysRateLimiter;
import com.example.admin.service.SysRateLimitService;
import com.example.common.core.entity.RateLimiterLevel;
import com.example.common.gateway.inteface.LimiterLevelResolver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author czx
 * @title: LimiterInitConfig
 * @projectName ms
 * @description: TODO 限流配置初始化
 * @date 2019/7/1515:14
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class LimiterInitConfig {

    private final SysRateLimitService sysRateLimitService;
    private final LimiterLevelResolver limiterLevelResolver;

    @Async
    @Order
    @EventListener({WebServerInitializedEvent.class})
    public void initLimit(){
        List<SysRateLimiter> list = sysRateLimitService.list();
        RateLimiterLevel rateLimiterLevel = new RateLimiterLevel();
        Map<String,Integer[]> map = new HashMap<>();
        list.forEach(sysRateLimiter -> {
            map.put(sysRateLimiter.getLevel(),new Integer[]{sysRateLimiter.getReplenishRate(),sysRateLimiter.getBurstCapacity(),sysRateLimiter.getLimitType()});
        });
        rateLimiterLevel.setLevels(map);
        limiterLevelResolver.save(rateLimiterLevel);
        log.info("==============限流配置初始化成功================");
    }

}
