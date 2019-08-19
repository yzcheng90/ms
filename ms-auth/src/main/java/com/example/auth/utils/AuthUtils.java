package com.example.auth.utils;

import cn.hutool.core.codec.Base64;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;

/**
 * @author czx
 * @title: AuthUtils
 * @projectName ms
 * @description: TODO
 * @date 2019/8/1916:26
 */
@UtilityClass
public class AuthUtils {

    public static final String BASIC_ = "Basic ";
    public static final String CLIENT_CREDENTIALS = "client_credentials";
    public static final String PASSWORD = "password";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String AUTHORIZATION_CODE = "authorization_code";
    public static final String SOCIAL = "social";

    public static final String AUTH_URL = "/oauth/token";
    public static final String SOCIAL_AUTH_URL = "/social/token/social";


    @SneakyThrows
    public String[] getClientDetails(String header){
        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("客户端信息无效");
        }

        String token = new String(decoded, StandardCharsets.UTF_8);

        int index = token.indexOf(":");
        if (index == -1) {
            throw new RuntimeException("客户端信息无效");
        }
        return new String[]{token.substring(0, index), token.substring(index + 1)};
    }

}
