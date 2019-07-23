package com.example.auth.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.example.auth.service.AuthenticationUserService;
import com.example.auth.store.CustomRedisTokenStore;
import com.example.common.core.constants.SecurityConstants;
import com.example.common.core.entity.R;
import com.example.common.core.entity.TokenEntity;
import com.example.common.resource.entity.CustomUserDetailsUser;
import com.example.common.user.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/token")
@AllArgsConstructor
public class TokenController {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private final RedisTemplate redisTemplate;
    private final TokenStore tokenStore;
    private final AuthenticationUserService authenticationUserService;
    /**
     * 认证页面
     *
     * @return ModelAndView
     */
    @GetMapping("/login")
    public ModelAndView require() {
        return new ModelAndView("ftl/login");
    }

    @RequestMapping(value = "{password}",method = RequestMethod.GET)
    public String encodePassword(@PathVariable("password") String password){
        return ENCODER.encode(password);
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public R getTokenList(){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        String prefix = SecurityConstants.MS_OAUTH_PREFIX + SecurityConstants.ACCESS + "*";
        Set<String> keys = redisTemplate.keys(prefix);
        List<TokenEntity> tokenEntities = new ArrayList<>();
        if(CollUtil.isNotEmpty(keys)){
            Iterator<String> accessKey = keys.iterator();
            while (accessKey.hasNext()) {
                String tokenString = accessKey.next();
                String accessToken = StrUtil.subAfter(tokenString, SecurityConstants.MS_OAUTH_PREFIX + SecurityConstants.ACCESS, true);
                OAuth2AccessToken token = tokenStore.readAccessToken(accessToken);
                TokenEntity tokenEntity = new TokenEntity();
                tokenEntity.setToken(token.getValue());
                tokenEntity.setExpiration(token.getExpiration());
                OAuth2Authentication oAuth2Auth = tokenStore.readAuthentication(token);
                Authentication authentication = oAuth2Auth.getUserAuthentication();
                tokenEntity.setClientId(oAuth2Auth.getOAuth2Request().getClientId());
                tokenEntity.setGrantType(oAuth2Auth.getOAuth2Request().getGrantType());
                if (authentication instanceof UsernamePasswordAuthenticationToken) {
                    UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
                    if (authenticationToken.getPrincipal() instanceof CustomUserDetailsUser) {
                        CustomUserDetailsUser user = (CustomUserDetailsUser) authenticationToken.getPrincipal();
                        tokenEntity.setUserId(user.getUserId());
                        tokenEntity.setUsername(user.getUsername());
                        tokenEntity.setLimitLevel(user.getLimitLevel());
                    }
                }
                tokenEntities.add(tokenEntity);
            }
        }
        return R.builder().data(tokenEntities).build();
    }

    @RequestMapping(value = "/updateLimitLevel/{token}/{level}",method = RequestMethod.GET)
    public R updateUserLimitLevel(@PathVariable("token") String token,@PathVariable("level") int level){
        OAuth2Authentication oAuth2Auth = tokenStore.readAuthentication(token);
        Authentication authentication = oAuth2Auth.getUserAuthentication();
        CustomUserDetailsUser customUser = (CustomUserDetailsUser) authentication.getPrincipal();
        customUser.setLimitLevel(level);
        ((CustomRedisTokenStore)tokenStore).updateUserLimitLevel(customUser,token);
        SysUser sysUser = new SysUser();
        sysUser.setUserId(customUser.getUserId());
        sysUser.setLimitLevel(level);
        authenticationUserService.updateById(sysUser);
        return R.builder().build();
    }
}
