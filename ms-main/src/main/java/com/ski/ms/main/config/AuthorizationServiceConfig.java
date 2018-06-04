package com.ski.ms.main.config;

import com.ski.ms.lib.constant.SecurityConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务器
 * Created by czx on 2018/4/23.
 */
@Order(Integer.MAX_VALUE - 8)
@Slf4j
@Configuration
@EnableAuthorizationServer
public class AuthorizationServiceConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthServiceConfig authServiceConfig;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 要认证的客户端配置
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        log.info("configure(ClientDetailsServiceConfigurer clients)==============加载");
        /**
         * 配置客户端详情信息(Client Details)
         * clientId：（必须的）用来标识客户的Id。
         * secret：（需要值得信任的客户端）客户端安全码，如果有的话。
         * scope：用来限制客户端的访问范围，如果为空（默认）的话，那么客户端拥有全部的访问范围。
         * authorizedGrantTypes：此客户端可以使用的授权类型，默认为空。
         * authorities：此客户端可以使用的权限（基于Spring Security authorities）。
         */
        clients.inMemory()
                .withClient(authServiceConfig.getClientId())
                .secret(authServiceConfig.getClientSecret())
                .scopes(authServiceConfig.getScope())
                .authorizedGrantTypes("client_credentials", "password", "refresh_token")
                .accessTokenValiditySeconds(1200)
                .refreshTokenValiditySeconds(50000);
    }

    /**
     * 使配置的token生效 redisTokenStore()
     * @param endpoints
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        log.info("configure(AuthorizationServerEndpointsConfigurer endpoints)==============加载");

        //token增强配置
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));

        endpoints
                // token存储
                .tokenStore(redisTokenStore())
                // 自定义token生成方案
                .tokenEnhancer(tokenEnhancerChain)
                //.accessTokenConverter(accessTokenConverter())
                // 身份认证管理器, 主要用于"password"授权模式
                .authenticationManager(authenticationManager)
                // 配合身份认证管理器, 检查用户名密码有效性
                .userDetailsService(userDetailsService); //告诉security 用自己的实现dao
    }


    /**
     * 认证服务器的安全配置
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        log.info("configure(AuthorizationServerSecurityConfigurer security)==============加载");
        security
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("isAuthenticated()") //访问tokenKey 的时候须要认证
                .checkTokenAccess("permitAll()");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //用户验证的时候使用加密处理
        return new BCryptPasswordEncoder();
    }


    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        AuthJwtAccessTokenConverter jwtAccessTokenConverter = new AuthJwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(SecurityConstant.SIGN_KEY);
        return jwtAccessTokenConverter;
    }

    /**
     * tokenstore 定制化处理
     *
     * @return TokenStore
     * 1. 如果使用的 redis-cluster 模式请使用 PigRedisTokenStore
     * PigRedisTokenStore tokenStore = new PigRedisTokenStore();
     * tokenStore.setRedisTemplate(redisTemplate);
     */
    @Bean
    public TokenStore redisTokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenStore.setPrefix(SecurityConstant.SK_PREFIX);
        return tokenStore;
    }

    /**
     * jwt 生成token 定制化处理
     * @return TokenEnhancer
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            final Map<String, Object> additionalInfo = new HashMap<>(1);
            additionalInfo.put("license", SecurityConstant.SK_LICENSE);
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }
}
