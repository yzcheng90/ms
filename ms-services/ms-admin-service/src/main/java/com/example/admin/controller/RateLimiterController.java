package com.example.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.entity.SysRateLimiter;
import com.example.admin.service.SysRateLimitService;
import com.example.common.core.base.AbstractController;
import com.example.common.core.entity.R;
import com.example.common.core.entity.RateLimiterLevel;
import com.example.common.core.entity.RateLimiterVO;
import com.example.common.gateway.inteface.LimiterLevelResolver;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public R saveRateLimit() {
        List<SysRateLimiter> limiters = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SysRateLimiter limiter = new SysRateLimiter();
            limiter.setLevel(String.valueOf((i + 1)));
            limiter.setBurstCapacity((i + 1) * 10);
            limiter.setReplenishRate((i + 1) * 10);
            limiter.setLimitType(1);
            limiters.add(limiter);
        }
        sysRateLimitService.saveRateLimit(limiters);
        return R.builder().build();
    }

    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public R getList(Page page) {
        return new R(sysRateLimitService.page(page));
    }

    @RequestMapping("/sync")
    public R syncRedisRateLimit() {
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
        return R.builder().build();
    }

}
