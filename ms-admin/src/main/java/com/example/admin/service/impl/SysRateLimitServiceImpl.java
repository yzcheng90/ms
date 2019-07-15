package com.example.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.entity.SysRateLimiter;
import com.example.admin.mapper.SysRateLimiterMapper;
import com.example.admin.service.SysRateLimitService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SysRateLimitServiceImpl extends ServiceImpl<SysRateLimiterMapper, SysRateLimiter> implements SysRateLimitService {


    @Override
    @Transactional
    public void saveRateLimit(List<SysRateLimiter> limiters) {
        limiters.forEach(sysRateLimiter -> baseMapper.insert(sysRateLimiter));
    }
}
