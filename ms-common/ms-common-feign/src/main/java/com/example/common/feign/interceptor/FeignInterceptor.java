package com.example.common.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author czx
 * @title: FeignInterceptor
 * @projectName ms
 * @description: TODO
 * @date 2019/7/2311:29
 */
@Slf4j
@Configuration
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("==============feign 拦截器===============",requestTemplate.headers());
    }
}
