package com.example.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.common.resource.entity.SysUser;

public interface AuthenticationUserMapper extends BaseMapper<SysUser> {

    SysUser loadUserByUsername(String username);
}
