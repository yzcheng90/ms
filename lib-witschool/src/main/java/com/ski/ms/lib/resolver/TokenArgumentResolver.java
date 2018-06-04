package com.ski.ms.lib.resolver;

import com.ski.ms.lib.constant.SecurityConstant;
import com.ski.ms.lib.entity.UserEntity;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * Token转化UserEntity
 */
@Slf4j
@Configuration
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 1. 入参筛选
     *
     * @param methodParameter 参数集合
     * @return 格式化后的参数
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(UserEntity.class);
    }

    /**
     * @param methodParameter       入参集合
     * @param modelAndViewContainer model 和 view
     * @param nativeWebRequest      web相关
     * @param webDataBinderFactory  入参解析
     * @return 包装对象
     * @throws Exception exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String username = request.getHeader(SecurityConstant.USER_HEADER);
        String roles = request.getHeader(SecurityConstant.ROLE_HEADER);
        if (StrUtil.isBlank(username) || StrUtil.isBlank(roles)) {
            log.warn("resolveArgument error username or role is empty");
            return null;
        } else {
            log.info("resolveArgument username :{} roles:{}", username, roles);
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
       /* List<SysRole> sysRoleList = new ArrayList<>();
        Arrays.stream(roles.split(",")).forEach(role -> {
            SysRole sysRole = new SysRole();
            sysRole.setRoleName(role);
            sysRoleList.add(sysRole);
        });
        userEntity.setRoleList(sysRoleList);*/
        return userEntity;
    }

}
