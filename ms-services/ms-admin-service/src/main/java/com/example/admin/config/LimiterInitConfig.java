package com.example.admin.config;

import cn.hutool.core.bean.BeanUtil;
import com.example.admin.entity.SysRateLimiter;
import com.example.admin.service.SysRateLimitService;
import com.example.common.core.entity.RateLimiterLevel;
import com.example.common.core.entity.RateLimiterVO;
import com.example.common.gateway.inteface.LimiterLevelResolver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;

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
    public void initLimit() {
        List<SysRateLimiter> list = sysRateLimitService.list();
        RateLimiterLevel rateLimiterLevel = new RateLimiterLevel();
        List<RateLimiterVO> limiterVOS = new ArrayList<>();
        list.forEach(sysRateLimiter -> {
            RateLimiterVO vo = new RateLimiterVO();
            BeanUtil.copyProperties(sysRateLimiter, vo);
            limiterVOS.add(vo);
        });
        rateLimiterLevel.setLevels(limiterVOS);
        limiterLevelResolver.save(rateLimiterLevel);
        log.info("==============限流配置初始化成功================");
    }

}
