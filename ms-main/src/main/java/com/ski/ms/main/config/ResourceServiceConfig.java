package com.ski.ms.main.config;

import com.ski.ms.lib.config.FilterUrlsPropertiesConfig;
import com.ski.ms.main.handler.AuthenticationFailureHandler;
import com.ski.ms.main.handler.MobileAuthenticationSuccessHandler;
import com.ski.ms.main.mobile.MobileSecurityConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * Created by czx on 2018/4/23.
 */
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER - 1)
@Slf4j
@Configuration
@EnableResourceServer
public class ResourceServiceConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private FilterUrlsPropertiesConfig filterUrlsPropertiesConfig;

    @Autowired
    private MobileSecurityConfigurer mobileSecurityConfigurer;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private MobileAuthenticationSuccessHandler mobileAuthenticationSuccessHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
                http.formLogin()
                        .loginPage("/authentication/require")  //自定义登录URL
                        .loginProcessingUrl("/authentication/form") //配置这个URL 是为了让 usernamepassword 的一个过滤器去处理 login post 请求
                        .successHandler(mobileAuthenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
                        .and()
                        .authorizeRequests();
        for (String url : filterUrlsPropertiesConfig.getAnon()) {
            registry.antMatchers(url).permitAll();  //当访问这些url 的时候不须要验证
            log.info("==============不须要验证的URL："+url);
        }
        registry.anyRequest().authenticated()
                .and()
                .csrf().disable();  //csrf.disable  关闭跨站 请求
        // 禁用缓存
        http.headers().cacheControl();
        http.apply(mobileSecurityConfigurer);
    }

}
