package com.ski.ms.common.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ski.ms.common.mapper.SysLogMapper;
import com.ski.ms.common.service.SysLogService;
import com.ski.ms.lib.constant.SecurityConstant;
import com.ski.ms.lib.entity.SysLog;
import com.ski.ms.lib.utils.Assert;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 日志表 服务实现类
 * Created by czx on 2018/4/24.
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Override
    public Boolean updateByLogId(Long id) {
        Assert.isNull(id, "日志ID为空");

        SysLog sysLog = new SysLog();
        sysLog.setId(id);
        sysLog.setDelFlag(SecurityConstant.STATUS_DEL);
        sysLog.setUpdateTime(new Date());
        return updateById(sysLog);
    }
}
