package com.example.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.auth.mapper.SysUserRoleMapper;
import com.example.auth.service.SysUserRoleService;
import com.example.common.user.entity.SysUserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * 用户与角色对应关系
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2019年7月30日 下午22:33:33
 */
@Service
@AllArgsConstructor
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper,SysUserRole> implements SysUserRoleService {

}
