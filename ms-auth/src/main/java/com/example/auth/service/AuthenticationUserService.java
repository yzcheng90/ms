package com.example.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.user.entity.SysUser;

/**
 * @Description //TODO 用户service$
 * @Date 21:06
 * @Author yzcheng90@qq.com
 **/
public interface AuthenticationUserService extends IService<SysUser> {

    /**
     * 查询用户信息
     * @param username 用户名
     * @return
     */
    SysUser loadUserByUsername(String username);
}
