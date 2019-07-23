package com.example.common.feign.interceptor;

import cn.hutool.core.util.StrUtil;
import com.example.common.core.constants.CommonConstants;
import com.example.common.core.constants.SecurityConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author czx
 * @title: FeignInterceptor
 * @projectName ms
 * @description: TODO
 * @date 2019/7/23 21:30
 */
@Slf4j
@Configuration
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String token = (String) request.getAttribute(CommonConstants.ACCESS_TOKEN_VALUE);
            String secretKey =  request.getHeader(SecurityConstants.SECRET_KEY);
            if(StrUtil.isNotBlank(token)){
                requestTemplate.header(CommonConstants.AUTHORIZATION,CommonConstants.BEARER + token);
            }
            if(StrUtil.isNotBlank(secretKey)){
                requestTemplate.header(SecurityConstants.SECRET_KEY,secretKey);
            }
        }
    }
}
