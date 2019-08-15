package com.example.auth.store;

import com.example.auth.service.CustomClientDetailsService;
import com.example.common.core.constants.SecurityConstants;
import com.example.common.core.entity.SysLoginLogVO;
import com.example.common.rabbitmq.producer.LoginLogProducer;
import com.example.common.resource.entity.CustomUserDetailsUser;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author czx
 * @title: CustomTokenEnhancer
 * @projectName ms
 * @description: TODO
 * @date 2019/7/2610:12
 */
public class CustomTokenEnhancer implements TokenEnhancer {

    private CustomClientDetailsService customClientDetailsService;

    @Autowired
    private LoginLogProducer loginLogProducer;

    public CustomTokenEnhancer(CustomClientDetailsService customClientDetailsService){
        this.customClientDetailsService = customClientDetailsService;
    }

    @SneakyThrows
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        SysLoginLogVO sysLoginLogVO = SysLoginLogVO
                .builder()
                .grant_type(authentication.getOAuth2Request().getGrantType())
                .request_uri(request.getRequestURI())
                .user_agent(request.getHeader("User-Agent"))
                .request_method(request.getMethod())
                .request_ip(InetAddress.getLocalHost().getHostAddress())
                .token(accessToken.getValue())
                .build();
        if (SecurityConstants.CLIENT_CREDENTIALS.equals(authentication.getOAuth2Request().getGrantType())) {
            ClientDetails clientDetails = customClientDetailsService.loadClientByClientId((String) authentication.getPrincipal());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(clientDetails.getAdditionalInformation());
            sysLoginLogVO.setClient_id((String) authentication.getPrincipal());
            loginLogProducer.send(sysLoginLogVO);
            return accessToken;
        }
        final Map<String, Object> additionalInfo = new HashMap<>(8);
        CustomUserDetailsUser customUserDetailsUser = (CustomUserDetailsUser) authentication.getUserAuthentication().getPrincipal();
        additionalInfo.put(SecurityConstants.USER_ID, customUserDetailsUser.getUserId());
        additionalInfo.put(SecurityConstants.LIMIT_LEVEL, customUserDetailsUser.getLimitLevel());
        additionalInfo.put(SecurityConstants.USER_NAME, customUserDetailsUser.getUsername());
        sysLoginLogVO.setUser_name(customUserDetailsUser.getUsername());
        loginLogProducer.send(sysLoginLogVO);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
