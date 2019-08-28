package com.example.auth.handler;

import com.example.auth.utils.AuthUtils;
import com.example.common.core.component.IPUtils;
import com.example.common.core.entity.SysLoginLogVO;
import com.example.common.rabbitmq.producer.LoginLogProducer;
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
    private LoginLogProducer loginLogProducer;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String grantType = request.getParameter(AuthUtils.GRANT_TYPE);
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String ip = IPUtils.getIpAddress(request);
        String detail = IPUtils.getAddressDetail(ip);
        SysLoginLogVO sysLoginLogVO = SysLoginLogVO
                .builder()
                .grant_type(grantType)
                .request_uri(request.getRequestURI())
                .user_agent(request.getHeader("User-Agent"))
                .request_method(request.getMethod())
                .request_ip(ip)
                .request_detail(detail)
                .build();
        Authentication authentication = authenticationSuccessEvent.getAuthentication();
        switch (grantType){
            case AuthUtils.CLIENT_CREDENTIALS:
                if(authentication.getPrincipal() instanceof User){
                    User user = (User) authentication.getPrincipal();
                    sysLoginLogVO.setClient_id(user.getUsername());
                }else if (authentication.getPrincipal() instanceof String){
                    sysLoginLogVO.setClient_id((String) authentication.getPrincipal());
                }
                loginLogProducer.send(sysLoginLogVO);
                break;
            case AuthUtils.REFRESH_TOKEN:
                break;
            default:
                if(authentication.getPrincipal() instanceof CustomUserDetailsUser){
                    CustomUserDetailsUser customUserDetailsUser = (CustomUserDetailsUser) authentication.getPrincipal();
                    sysLoginLogVO.setUser_name(customUserDetailsUser.getUsername());
                    String[] tokens = AuthUtils.getClientDetails(header);
                    assert tokens.length == 2;
                    String clientId = tokens[0];
                    sysLoginLogVO.setClient_id(clientId);
                    loginLogProducer.send(sysLoginLogVO);
                }
                break;
        }
    }
}
