package com.example.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.auth.mapper.AuthenticationUserMapper;
import com.example.auth.service.AuthenticationUserService;
import com.example.common.user.entity.SysUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Description //TODO 用户service$
 * @Date 21:08
 * @Author yzcheng90@qq.com
 **/
@Service
@AllArgsConstructor
public class AuthenticationUserServiceImpl extends ServiceImpl<AuthenticationUserMapper, SysUser> implements AuthenticationUserService {

    @Override
    public SysUser loadUserByUsername(String username) {
        return baseMapper.loadUserByUsername(username);
    }
}
