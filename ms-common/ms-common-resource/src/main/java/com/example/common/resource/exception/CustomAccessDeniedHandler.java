package com.example.common.resource.exception;

import cn.hutool.json.JSONUtil;
import com.example.common.core.entity.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author czx
 * @title: CustomAccessDeniedHandler
 * @projectName ms
 * @description: TODO 没有权限 自定义返回
 * @date 2019/8/211:44
 */
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.getWriter().write(
                JSONUtil.toJsonStr(R
                        .builder()
                        .code(HttpStatus.FORBIDDEN.value())
                        .message(accessDeniedException.getMessage())
                        .build()));
    }
}
