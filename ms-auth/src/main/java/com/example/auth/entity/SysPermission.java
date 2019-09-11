package com.example.auth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @Description //TODO $系统权限表
 * @Date 2019/8/6 22:59
 * @Author yzcheng90@qq.com
 **/

@Data
@TableName("sys_permission")
@EqualsAndHashCode(callSuper = false)
public class SysPermission extends Model<SysPermission> {

    @TableId
    private int id;

    /**
     * 权限名称
     **/
    private String name;
    /**
     * 权限英文名称
     **/
    private String permission;
    /**
     * 授权路径
     **/
    private String url;
    /**
     * 备注
     **/
    private String description;
    /**
     * 创建时间
     **/
    private LocalDateTime createTime;
    /**
     * 更新时间
     **/
    private LocalDateTime updateTime;
    /**
     * 服务ID
     **/
    private String serviceId;
}
