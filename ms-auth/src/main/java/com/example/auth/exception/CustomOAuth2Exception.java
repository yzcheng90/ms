package com.example.auth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @Description //TODO 重写定制为标准《R》 OAuth2Exception$
 * @Date 21:34
 * @Author yzcheng90@qq.com
 **/
@JsonSerialize(using = OAuth2ExceptionSerializer.class)
public class CustomOAuth2Exception extends OAuth2Exception{

    @Getter
    private String errorType;

    public CustomOAuth2Exception(String msg) {
        super(msg);
    }

    public CustomOAuth2Exception(String errorType, String msg) {
        super(msg);
        this.errorType = errorType;
    }

    public CustomOAuth2Exception(String errorType,String msg, Throwable t) {
        super(msg, t);
        this.errorType = errorType;
    }
}
