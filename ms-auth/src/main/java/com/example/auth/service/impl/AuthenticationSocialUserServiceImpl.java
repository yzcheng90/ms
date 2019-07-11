package com.example.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.auth.mapper.AuthenticationSocialUserMapper;
import com.example.auth.service.AuthenticationSocialUserService;
import com.example.auth.social.LoginHandler;
import com.example.auth.utils.UserDetailsUtils;
import com.example.common.resource.entity.SysSocialDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description //TODO 第三方用户信息serivce$
 * @Date 21:07
 * @Author yzcheng90@qq.com
 **/
@Service
@AllArgsConstructor
public class AuthenticationSocialUserServiceImpl extends ServiceImpl<AuthenticationSocialUserMapper,SysSocialDetails> implements AuthenticationSocialUserService {

    private final Map<String, LoginHandler> loginHandlerMap;

    @Override
    public UserDetails getSocialUserInfo(String key) {
        String[] index = key.split("@");
        String type = index[0];
        String loginStr = index[1];
        return UserDetailsUtils.getUserDetails(loginHandlerMap.get(type).login(loginStr).getUser());
    }
}
