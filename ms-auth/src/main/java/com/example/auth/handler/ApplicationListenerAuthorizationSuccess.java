package com.example.auth.handler;

import cn.hutool.core.util.StrUtil;
import com.example.auth.entity.SysLoginLog;
import com.example.auth.service.SysLoginLogService;
import com.example.auth.utils.AuthUtils;
import com.example.common.core.component.IPUtils;
import com.example.common.resource.entity.CustomUserDetailsUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author czx
 * @title: ApplicationListenerAuthorizationSuccess
 * @projectName ms
 * @description: TODO 源码认证的流程，认证结束后publish一个事件
 * @date 2019/8/289:33
 */
@Slf4j
@Component
public class ApplicationListenerAuthorizationSuccess implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private SysLoginLogService sysLoginLogService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String grantType = request.getParameter(AuthUtils.GRANT_TYPE);
        if(StrUtil.isBlank(grantType)){
            return;
        }
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String ip = IPUtils.getIpAddress(request);
        // String detail = IPUtils.getAddressDetail(ip);  调用接口 可能超时...导致认证可能出错 或调用时间太长，后面做一个定时任务去做吧
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setGrant_type(grantType);
        sysLoginLog.setRequest_uri(request.getRequestURI());
        sysLoginLog.setUser_agent(request.getHeader("User-Agent"));
        sysLoginLog.setRequest_method(request.getMethod());
        sysLoginLog.setRequest_ip(ip);
        //sysLoginLog.setRequest_detail(detail);
        Authentication authentication = authenticationSuccessEvent.getAuthentication();
        switch (grantType){
            case AuthUtils.CLIENT_CREDENTIALS:
                if(authentication.getPrincipal() instanceof User){
                    User user = (User) authentication.getPrincipal();
                    sysLoginLog.setClient_id(user.getUsername());
                }else if (authentication.getPrincipal() instanceof String){
                    sysLoginLog.setClient_id((String) authentication.getPrincipal());
                }
                sysLoginLogService.save(sysLoginLog);
                break;
            case AuthUtils.REFRESH_TOKEN:
                break;
            default:
                if(authentication.getPrincipal() instanceof CustomUserDetailsUser){
                    CustomUserDetailsUser customUserDetailsUser = (CustomUserDetailsUser) authentication.getPrincipal();
                    sysLoginLog.setUser_name(customUserDetailsUser.getUsername());
                    String[] tokens = AuthUtils.getClientDetails(header);
                    assert tokens.length == 2;
                    String clientId = tokens[0];
                    sysLoginLog.setClient_id(clientId);
                    sysLoginLogService.save(sysLoginLog);
                }
                break;
        }
    }
}
