package com.ski.ms.main.service;

import com.ski.ms.lib.entity.UserEntity;
import com.ski.ms.main.hystrix.UserService;
import com.ski.ms.main.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Created by czx on 2018/4/24.
 */
@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity =  userService.findUserByUsername(s);
        UserDetailsImpl userDetails = new UserDetailsImpl(userEntity);
        return userDetails;
    }
}
