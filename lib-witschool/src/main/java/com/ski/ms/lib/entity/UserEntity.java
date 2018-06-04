package com.ski.ms.lib.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息
 * Created by czx on 2018/4/23.
 */
@Data
public class UserEntity implements Serializable{
    /**
     * 主键ID
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 密码
     */
    private String password;
    /**
     * 随机盐
     */
    private String salt;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 0-正常，1-删除
     */
    private String delFlag;
    /**
     * 简介
     */
    private String introduction;


}
