package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.entity.SysRateLimiter;

import java.util.List;

public interface SysRateLimitService extends IService<SysRateLimiter> {

    void saveRateLimit(List<SysRateLimiter> limiters);
}
