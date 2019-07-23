package com.example.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.mapper.SysUserMapper;
import com.example.admin.service.SysUserService;
import com.example.common.user.entity.SysUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public SysUser getUserById(Integer id) {
        return baseMapper.getUserById(id);
    }
}
