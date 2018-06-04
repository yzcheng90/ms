package com.ski.ms.lib.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 日志类
 */
@Data
public class LogVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private SysLog sysLog;
    private String username;
}
