package com.example.admin.controller;

import com.example.admin.entity.SysRateLimiter;
import com.example.admin.service.SysRateLimitService;
import com.example.common.core.base.AbstractController;
import com.example.common.core.entity.R;
import com.example.common.core.entity.RateLimiterLevel;
import com.example.common.gateway.inteface.LimiterLevelResolver;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author czx
 * @title: RateLimiterController
 * @projectName ms
 * @description: TODO
 * @date 2019/7/1511:44
 */
@RequestMapping("/limit")
@RestController
@AllArgsConstructor
public class RateLimiterController extends AbstractController {

    private final SysRateLimitService sysRateLimitService;
    private final LimiterLevelResolver limiterLevelResolver;

    @RequestMapping(value = "/save",method = RequestMethod.GET)
    public R saveRateLimit(){
        List<SysRateLimiter> limiters = new ArrayList<>();
        for (int i=0;i<5;i++){
            SysRateLimiter limiter = new SysRateLimiter();
            limiter.setLevel(String.valueOf((i + 1)));
            limiter.setBurstCapacity(( i + 1 ) * 10);
            limiter.setReplenishRate(( i + 1 ) * 10);
            limiter.setLimitType(1);
            limiters.add(limiter);
        }
        sysRateLimitService.saveRateLimit(limiters);
        return R.builder().build();
    }

    @RequestMapping("/sync")
    public R syncRedisRateLimit(){
        List<SysRateLimiter> list = sysRateLimitService.list();
        RateLimiterLevel rateLimiterLevel = new RateLimiterLevel();
        Map<String,Integer[]> map = new HashMap<>();
        list.forEach(sysRateLimiter -> {
            map.put(sysRateLimiter.getLevel(),new Integer[]{sysRateLimiter.getReplenishRate(),sysRateLimiter.getBurstCapacity(),sysRateLimiter.getLimitType()});
        });
        rateLimiterLevel.setLevels(map);
        limiterLevelResolver.save(rateLimiterLevel);
        return R.builder().build();
    }

}
