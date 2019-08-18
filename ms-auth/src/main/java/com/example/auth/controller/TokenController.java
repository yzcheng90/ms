package com.example.auth.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auth.service.AuthenticationOauthClientService;
import com.example.auth.service.AuthenticationSocialUserService;
import com.example.auth.service.AuthenticationUserService;
import com.example.auth.store.CustomRedisTokenStore;
import com.example.common.core.constants.SecurityConstants;
import com.example.common.core.entity.R;
import com.example.common.core.entity.StoreUser;
import com.example.common.core.entity.TokenEntity;
import com.example.common.resource.entity.CustomUserDetailsUser;
import com.example.common.user.entity.SysOauthClientDetails;
import com.example.common.user.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
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

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/token")
@AllArgsConstructor
public class TokenController {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private final RedisTemplate redisTemplate;
    private final TokenStore tokenStore;
    private final AuthenticationUserService authenticationUserService;
    private final AuthenticationOauthClientService authenticationOauthClientService;
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

    @RequestMapping(value = "/getClientList",method = RequestMethod.GET)
    public R getClientList(Page page){
        IPage<SysOauthClientDetails> iPage = authenticationOauthClientService.page(page);
        return R.ok().setData(iPage);
    }

    /**
     * 生产慎用  https://blog.csdn.net/lsblsb/article/details/73692771
     * @param page
     * @return
     */
    @SneakyThrows
    @RequestMapping(value = "/getTokenList",method = RequestMethod.GET)
    public R getTokenList(Page page){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        String prefix = SecurityConstants.MS_OAUTH_PREFIX + SecurityConstants.ACCESS + "*";
        ScanOptions options = ScanOptions.scanOptions().match(prefix).build();
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        RedisConnection rc = factory.getConnection();
        Cursor<byte[]> cursor = rc.scan(options);
        List<TokenEntity> tokenEntities = new ArrayList<>((int)page.getSize());
        int tmpIndex = 0;
        int startIndex = (int) ((page.getCurrent() - 1) * page.getSize());
        int end = (int) (page.getCurrent() * page.getSize());
        while (cursor.hasNext()) {
            if (tmpIndex >= startIndex && tmpIndex < end) {
                String value = new String(cursor.next());
                String accessToken = StrUtil.subAfter(value, SecurityConstants.MS_OAUTH_PREFIX + SecurityConstants.ACCESS, true);
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
                tmpIndex++;
                continue;
            }

            // 获取到满足条件的数据后,就可以退出了
            if(tmpIndex >=end) {
                break;
            }

            tmpIndex++;
            cursor.next();
        }

        RedisConnectionUtils.releaseConnection(rc, factory);
        page.setRecords(tokenEntities);
        page.setTotal(redisTemplate.keys(prefix).size());
        return R.ok().setData(page);
    }


    @RequestMapping(value = "/updateLimitLevel/{token}/{level}",method = RequestMethod.GET)
    public R updateUserLimitLevel(@PathVariable("token") String token,@PathVariable("level") int level){
        OAuth2Authentication oAuth2Auth = tokenStore.readAuthentication(token);
        if(ObjectUtil.isNull(oAuth2Auth)) return R.errorParam(token);
        Authentication authentication = oAuth2Auth.getUserAuthentication();
        String grantType = oAuth2Auth.getOAuth2Request().getGrantType();
        StoreUser storeUser = new StoreUser();
        switch (grantType){
            case SecurityConstants.CLIENT_CREDENTIALS:
                storeUser.setLimitLevel(level);
                storeUser.setUserId(oAuth2Auth.getOAuth2Request().getClientId());
                storeUser.setUserName(oAuth2Auth.getOAuth2Request().getClientId());
                SysOauthClientDetails details = new SysOauthClientDetails();
                details.setClientId(oAuth2Auth.getOAuth2Request().getClientId());
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put(SecurityConstants.LIMIT_LEVEL,level);
                details.setAdditionalInformation(JSONUtil.toJsonStr(hashMap));
                authenticationOauthClientService.updateById(details);
                break;
            default:
                CustomUserDetailsUser customUser = (CustomUserDetailsUser) authentication.getPrincipal();
                storeUser = StoreUser.builder().userId(customUser.getUserId()).limitLevel(customUser.getLimitLevel()).userName(customUser.getUsername()).build();
                SysUser sysUser = new SysUser();
                sysUser.setUserId(customUser.getUserId());
                sysUser.setLimitLevel(level);
                authenticationUserService.updateById(sysUser);
                break;
        }
        ((CustomRedisTokenStore)tokenStore).updateUserLimitLevel(storeUser,token);
        return R.ok();
    }
}
