package com.example.auth.config;

import com.example.common.resource.config.AuthIgnoreConfig;
import com.example.common.resource.config.ResourcePermissionConfig;
import com.example.common.resource.exception.CustomAccessDeniedHandler;
import com.example.common.resource.exception.ResourceAuthExceptionEntryPoint;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author czx
 * @title: ResourceServerConfig
 * @projectName ms
 * @description: TODO 这个资源服务是单独管理 auth 服务用的
 * @date 2019/9/11 14:34
 */
@Configuration
@EnableResourceServer
@AllArgsConstructor
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private final TokenStore tokenStore;
    private final ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final AuthIgnoreConfig authIgnoreConfig;
    private final ResourcePermissionConfig resourcePermissionConfig;

    /**
     * @Description //TODO http 请求的一些过滤配置,和权限配置
     **/
    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] urls = authIgnoreConfig.getIgnoreUrls().stream().distinct().toArray(String[]::new);
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        registry.antMatchers(urls).permitAll();
        resourcePermissionConfig.getPermissionEntities().forEach(permissionEntity ->{
            registry.antMatchers(permissionEntity.getUrl()).hasAnyRole(permissionEntity.getPermission());
        });
        registry.anyRequest().authenticated().and().csrf().disable();
    }

    /**
     * @Description //TODO 通过分配的resourcesId 和 token 去check_token 获取用户信息
     **/
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(resourceAuthExceptionEntryPoint);
        resources.accessDeniedHandler(customAccessDeniedHandler);
        resources.tokenStore(tokenStore);
    }
}
