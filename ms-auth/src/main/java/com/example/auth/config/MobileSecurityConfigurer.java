package com.example.auth.config;

import com.example.auth.mobile.MobileAuthenticationFilter;
import com.example.auth.mobile.MobileAuthenticationProvider;
import com.example.auth.service.AuthenticationSocialUserService;
import com.example.common.resource.security.ResourceAuthExceptionEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * oauth2 图解 https://www.cnblogs.com/mxmbk/p/9952298.html
 *
 * 自定认证模式 需要继承 SecurityConfigurerAdapter 重写 configure
 * 1、配置 过滤器 ,oauth2 默认认证过滤器是 ClientCredentialsTokenEndpointFilter 继承于 AbstractAuthenticationProcessingFilter
 *    自定义认证模式 也要配置过滤器，那就是自己写一个过滤器 继承 AbstractAuthenticationProcessingFilter 就可以
 *
 * 2、配置 认证提供者 的 provider oauth2 默认的 DaoAuthenticationProvider 继承于 AuthenticationProvider
 *    自定义认证模式 也要配置provider 和过滤器类似 自己写一个类 继承于 AuthenticationProvider 重写 authenticate 实现
 * @Description //TODO 手机号码登录验证配置$
 * @Date 20:13
 * @Author yzcheng90@qq.com
 **/

@Component
public class MobileSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationSocialUserService authenticationSocialUserService;

    @Getter
    @Setter
    private AuthenticationSuccessHandler mobileLoginSuccessHandler;

    @Override
    public void configure(HttpSecurity http) {
        MobileAuthenticationFilter mobileAuthenticationFilter = new MobileAuthenticationFilter();
        mobileAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        mobileAuthenticationFilter.setAuthenticationSuccessHandler(mobileLoginSuccessHandler);
        mobileAuthenticationFilter.setAuthenticationEntryPoint(new ResourceAuthExceptionEntryPoint(objectMapper));

        MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
        mobileAuthenticationProvider.setAuthenticationSocialUserService(authenticationSocialUserService);
        http.
            authenticationProvider(mobileAuthenticationProvider)
            .addFilterAfter(mobileAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
