package com.example.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.common.user.entity.SysUser;

public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser getUserById(Integer id);
}
