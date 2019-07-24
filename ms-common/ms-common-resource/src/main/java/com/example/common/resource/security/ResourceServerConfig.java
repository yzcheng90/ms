package com.example.common.resource.security;

import com.example.common.resource.config.AuthIgnoreConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @Author czx
 * @Description //TODO 资源服务器配置
 * @Date 17:03 2019/4/3
 **/
@Slf4j
@AllArgsConstructor
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private final RemoteTokenServices remoteTokenServices;
    private final RestTemplate lbRestTemplate;
    private final ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;
    private final AuthIgnoreConfig authIgnoreConfig;
    /**
     * @Description //TODO http 请求的一些过滤配置
     **/
    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] urls = authIgnoreConfig.getIgnoreUrls().stream().distinct().toArray(String[]::new);
        http
            .headers().frameOptions().disable()
            .and()
            .authorizeRequests().antMatchers(urls).permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf().disable();
    }

    /**
     * @Description //TODO 通过分配的resourcesId 和 token 去check_token 获取用户信息
     **/
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {

        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        UserAuthenticationConverter userTokenConverter = new CustomUserAuthenticationConverter();
        accessTokenConverter.setUserTokenConverter(userTokenConverter);

        remoteTokenServices.setRestTemplate(lbRestTemplate);
        remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
        resources.authenticationEntryPoint(resourceAuthExceptionEntryPoint);
        resources.tokenServices(remoteTokenServices);
    }
}
