package com.example.auth.config;

import com.example.auth.handler.MobileLoginSuccessHandler;
import com.example.auth.service.AuthenticationSocialUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @Author czx
 * @Description //TODO 认证配置
 * 1、指定登录页面
 * 2、指定验证手机登录客户端Details
 * 3、指定验证手机登录用户Details
 * 4、指定验证手机登录用户 SecurityConfigurerAdapter
 * @Date 17:09 2019/4/2
 * @Param
 * @return
 **/
@Primary
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ClientDetailsService clientDetailsService;

    @Lazy
    @Autowired
    private AuthorizationServerTokenServices defaultAuthorizationServerTokenServices;

    /**
     * @Description //TODO 配置详解 https://blog.csdn.net/yin380697242/article/details/51893397
     **/
    @SneakyThrows
    @Override
    protected void configure(HttpSecurity http) {
        http
            .authorizeRequests()
            .antMatchers(
                    "/token/login", // 不拦截
                    "/token/form", // 不拦截
                    "/actuator/**", //actuator（监控） 开头不拦截
                    "/mobile/**")  //mobile（手机） 开头不拦截
            .permitAll()
            .anyRequest().authenticated()
            .and().csrf().disable()
            .formLogin()
            .loginPage("/token/login") //配置统一登录页 重定向到 /token/login
            .loginProcessingUrl("/token/form") //配置统一登录方法提交方法
            .and()
            .apply(mobileSecurityConfigurer()); //添加mobile 认证配置
    }

    /**
     * 手机号登录成功后使用 defaultAuthorizationServerTokenServices 生成token返回
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler mobileLoginSuccessHandler() {
        return MobileLoginSuccessHandler.builder()
                .objectMapper(objectMapper)
                .clientDetailsService(clientDetailsService)
                .passwordEncoder(passwordEncoder())
                .defaultAuthorizationServerTokenServices(defaultAuthorizationServerTokenServices)
                .build();
    }

    /**
     * 配置手机号登录验证
     * @return
     */
    @Bean
    public MobileSecurityConfigurer mobileSecurityConfigurer() {
        MobileSecurityConfigurer mobileSecurityConfigurer = new MobileSecurityConfigurer();
        mobileSecurityConfigurer.setMobileLoginSuccessHandler(mobileLoginSuccessHandler());
        return mobileSecurityConfigurer;
    }

    /**
     * 不拦截静态资源
     *
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**");
    }

    /**
     * @Description //TODO 密码模式 必须要这个AuthenticationManager bean
     * @return org.springframework.security.authentication.AuthenticationManager
     **/
    @Bean
    @Override
    @SneakyThrows
    public AuthenticationManager authenticationManagerBean() {
        return super.authenticationManagerBean();
    }

    /**
     * https://spring.io/blog/2017/11/01/spring-security-5-0-0-rc1-released#password-storage-updated
     * Encoded password does not look like BCrypt
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
