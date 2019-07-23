package com.example.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.common.user.entity.SysUser;

public interface AuthenticationUserMapper extends BaseMapper<SysUser> {

    SysUser loadUserByUsername(String username);
}
