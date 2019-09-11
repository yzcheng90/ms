package com.example.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.auth.entity.SysLoginLog;
import com.example.auth.mapper.SysLoginLogMapper;
import com.example.auth.service.SysLoginLogService;
import org.springframework.stereotype.Service;


@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {
	
}
