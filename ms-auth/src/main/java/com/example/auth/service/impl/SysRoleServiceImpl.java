package com.example.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.auth.mapper.SysRoleMapper;
import com.example.auth.service.SysRoleService;
import com.example.common.user.entity.SysRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * 角色
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2019年7月30日 下午22:33:33
 */
@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper,SysRole> implements SysRoleService {

}
