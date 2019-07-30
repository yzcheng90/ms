package com.example.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.auth.mapper.SysRoleMapper;
import com.example.auth.mapper.SysUserRoleMapper;
import com.example.auth.service.AuthenticationUserService;
import com.example.auth.service.CustomUserDetailsService;
import com.example.auth.utils.UserDetailsUtils;
import com.example.common.core.constants.SecurityConstants;
import com.example.common.user.entity.SysRole;
import com.example.common.user.entity.SysUser;
import com.example.common.user.entity.SysUserRole;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * oauth 验证用户
 */
@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    private final AuthenticationUserService customUserService;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMapper sysRoleMapper;

    /**
     *  通过userName去调用service 验证用户
     * @param username userName
     * @return 验证成功的用户details
     * @throws UsernameNotFoundException
     */
    @Override
    @Cacheable(value = SecurityConstants.CACHE_USER_DETAILS, key = "#user_details", unless = "#result == null")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = customUserService.loadUserByUsername(username);
        List<Long> roleIds = sysUserRoleMapper
                .selectList(Wrappers.<SysUserRole>query().lambda().eq(SysUserRole::getUserId,sysUser.getUserId()))
                .stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
        if(CollUtil.isNotEmpty(roleIds)){
            List<String> roleCodes = sysRoleMapper.selectList(Wrappers.<SysRole>query().lambda().in(SysRole::getRoleId,roleIds))
                    .stream()
                    .map(SysRole::getRoleCode)
                    .collect(Collectors.toList());
            sysUser.setRoleCode(roleCodes);
        }
        UserDetails userDetails = UserDetailsUtils.getUserDetails(sysUser);
        return userDetails;
    }




}
