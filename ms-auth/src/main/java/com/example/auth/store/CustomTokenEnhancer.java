package com.example.auth.store;

import com.example.auth.service.CustomClientDetailsService;
import com.example.common.core.constants.SecurityConstants;
import com.example.common.resource.entity.CustomUserDetailsUser;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

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


    public CustomTokenEnhancer(CustomClientDetailsService customClientDetailsService){
        this.customClientDetailsService = customClientDetailsService;
    }

    @SneakyThrows
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (SecurityConstants.CLIENT_CREDENTIALS.equals(authentication.getOAuth2Request().getGrantType())) {
            ClientDetails clientDetails = customClientDetailsService.loadClientByClientId((String) authentication.getPrincipal());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(clientDetails.getAdditionalInformation());
            return accessToken;
        }
        final Map<String, Object> additionalInfo = new HashMap<>(8);
        CustomUserDetailsUser customUserDetailsUser = (CustomUserDetailsUser) authentication.getUserAuthentication().getPrincipal();
        additionalInfo.put(SecurityConstants.USER_ID, customUserDetailsUser.getUserId());
        additionalInfo.put(SecurityConstants.LIMIT_LEVEL, customUserDetailsUser.getLimitLevel());
        additionalInfo.put(SecurityConstants.USER_NAME, customUserDetailsUser.getUsername());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
