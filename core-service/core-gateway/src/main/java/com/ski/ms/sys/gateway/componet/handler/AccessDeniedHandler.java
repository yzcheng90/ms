package com.ski.ms.sys.gateway.componet.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ski.ms.lib.constant.SecurityConstant;
import com.ski.ms.lib.exception.DeniedException;
import com.ski.ms.lib.utils.R;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 授权拒绝处理器，覆盖默认的OAuth2AccessDeniedHandler
 * 包装失败信息到DeniedException
 * Created by czx on 2018/4/24.
 */
@Log4j
@Component
public class AccessDeniedHandler extends OAuth2AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 授权拒绝处理，使用R包装
     *
     * @param request       request
     * @param response      response
     * @param authException authException
     * @throws IOException      IOException
     * @throws ServletException ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) throws IOException, ServletException {
        logger.info("授权失败，禁止访问");
        response.setCharacterEncoding(SecurityConstant.UTF8);
        response.setContentType(SecurityConstant.CONTENT_TYPE);
        R<String> result = new R<>(new DeniedException("授权失败，禁止访问"));
        response.setStatus(403);
        PrintWriter printWriter = response.getWriter();
        printWriter.append(objectMapper.writeValueAsString(result));
    }
}
