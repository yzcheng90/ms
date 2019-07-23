package com.example.auth.service.impl;

import com.example.auth.service.AuthenticationUserService;
import com.example.auth.service.CustomUserDetailsService;
import com.example.auth.utils.UserDetailsUtils;
import com.example.common.core.constants.SecurityConstants;
import com.example.common.user.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * oauth 验证用户
 */
@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    private final AuthenticationUserService customUserService;

    /**
     *  通过userName去调用service 验证用户
     * @param username userName
     * @return 验证成功的用户details
     * @throws UsernameNotFoundException
     */
    @Override
    @Cacheable(value = SecurityConstants.CACHE_USER_DETAILS, key = "#user_details", unless = "#result == null")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser result = customUserService.loadUserByUsername(username);
        UserDetails userDetails = UserDetailsUtils.getUserDetails(result);
        return userDetails;
    }




}
