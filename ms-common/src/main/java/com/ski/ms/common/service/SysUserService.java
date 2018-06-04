package com.ski.ms.common.service;

import com.baomidou.mybatisplus.service.IService;
import com.ski.ms.common.model.SysUser;
import com.ski.ms.lib.entity.UserEntity;
import com.ski.ms.lib.utils.R;


/**
 * 用户表 service 接口
 * Created by czx on 2018/4/24.
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 通过用户名查询用户信息（含有角色信息）
     *
     * @param username 用户名
     * @return UserEntity
     */
    UserEntity selectUserEntityByUsername(String username);

    /**
     * 通过用户名查询用户信息（含有角色信息）V2
     *
     * @param username 用户名
     * @return UserEntity
     */
    UserEntity selectUserEntityByUsername_v2(String username);


    /**
     * 通过手机号查询用户信息（含有角色信息）
     *
     * @param mobile 用户名
     * @return UserEntity
     */
    UserEntity selectUserEntityByMobile(String mobile);


    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return UserEntity
     */
    UserEntity selectUserEntityById(Integer id);


    /**
     * 保存验证码
     *  @param randomStr 随机串
     * @param imageCode 验证码*/
    void saveImageCode(String randomStr, String imageCode);

    /**
     * 发送验证码
     * @param mobile 手机号
     * @return R
     */
    R<Boolean> sendSmsCode(String mobile);
}