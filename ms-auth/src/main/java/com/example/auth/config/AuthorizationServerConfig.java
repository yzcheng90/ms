package com.example.auth.config;


import com.example.auth.store.CustomRedisTokenStore;
import com.example.auth.exception.CustomWebResponseExceptionTranslator;
import com.example.auth.service.CustomClientDetailsService;
import com.example.auth.service.CustomUserDetailsService;
import com.example.common.core.constants.SecurityConstants;
import com.example.common.resource.entity.CustomUserDetailsUser;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务器配置
 *
 */
@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private final DataSource dataSource;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManagerBean;
    private final RedisConnectionFactory redisConnectionFactory;

    /**
     * 配置OAuth2的客户端相关信息
     * 该方法是用来验证clientId 和 clientSecret的
     * 所以要自定义查询客户端detailsService 去数据库
     * 去查询数据库这个客户端是否存在
     * @param clients
     */
    @Override
    @SneakyThrows
    public void configure(ClientDetailsServiceConfigurer clients) {
        CustomClientDetailsService clientDetailsService = new CustomClientDetailsService(dataSource);
        clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
        clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 配置AuthorizationServer安全认证的相关信息，创建ClientCredentialsTokenEndpointFilter核心过滤器
     * @Description //TODO  这个方法不配置的话 在check_token 的时候会报 禁止访问
     **/
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .allowFormAuthenticationForClients()
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * 配置AuthorizationServerEndpointsConfigurer众多相关类，包括配置身份认证器，配置认证方式，TokenStore，TokenGranter，OAuth2RequestFactory
     * 该方法是用来配置Authorization Server endpoints的一些非安全特性的
     * 比如token存储、token自定义、授权类型等等的
     * @param endpoints
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancer())
                .userDetailsService(customUserDetailsService)
                .authenticationManager(authenticationManagerBean)
                .reuseRefreshTokens(false)
                .exceptionTranslator(new CustomWebResponseExceptionTranslator());
    }

    /**
     * 指定tokenStore 为 redis
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        CustomRedisTokenStore tokenStore = new CustomRedisTokenStore(redisConnectionFactory);
        tokenStore.setPrefix(SecurityConstants.MS_OAUTH_PREFIX);
        return tokenStore;
    }

    /**
     * token 属性附加。
     * @return TokenEnhancer
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            if (SecurityConstants.CLIENT_CREDENTIALS.equals(authentication.getOAuth2Request().getGrantType())) {
                return accessToken;
            }

            final Map<String, Object> additionalInfo = new HashMap<>(8);
            CustomUserDetailsUser customUserDetailsUser = (CustomUserDetailsUser) authentication.getUserAuthentication().getPrincipal();
            additionalInfo.put(SecurityConstants.USER_ID, customUserDetailsUser.getUserId());
            additionalInfo.put(SecurityConstants.LIMIT_LEVEL, customUserDetailsUser.getLimitLevel());
            additionalInfo.put(SecurityConstants.USER_NAME, customUserDetailsUser.getUsername());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

}
