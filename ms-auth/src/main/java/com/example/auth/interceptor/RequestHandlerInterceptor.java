package com.example.auth.interceptor;

import com.example.common.cache.component.RedisUUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author czx
 * @title: RequestHandlerInterceptor
 * @projectName ms
 * @description: TODO 正常登录验证请求
 * @date 2019/8/1915:42
 */
@Slf4j
@AllArgsConstructor
public class RequestHandlerInterceptor implements HandlerInterceptor {

    private final RedisUUID redisUUID;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("================RequestHandlerInterceptor来了========================");
        return true;
    }
}
