package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * @author czx
 * @title: SysRateLimiter
 * @projectName ms
 * @description: TODO 限流配置表
 * @date 2019/7/1511:39
 */

@Data
@TableName("sys_rate_limiter")
public class SysRateLimiter extends Model<SysRateLimiter> {

    @TableId
    private int limitId;

    /**
     * 等极
     **/
    private String level;

    /**
     * 流速
     **/
    private int replenishRate;

    /**
     * 桶容量
     **/
    private int burstCapacity;

    /**
     * 单位 1:秒，2:分钟，3:小时，4:天
     **/
    private int limitType;

}
