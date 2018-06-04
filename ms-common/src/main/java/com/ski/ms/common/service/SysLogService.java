package com.ski.ms.common.service;

import com.baomidou.mybatisplus.service.IService;
import com.ski.ms.lib.entity.SysLog;

/**
 * <p>
 * 日志表 服务类
 * </p>
 * Created by czx on 2018/4/24.
 */
public interface SysLogService extends IService<SysLog> {

    /**
     * 通过ID删除日志（逻辑删除）
     *
     * @param id 日志ID
     * @return true/false
     */
    Boolean updateByLogId(Long id);
}
