package com.ski.ms.sys.gateway.service.impl;

import com.ski.ms.sys.gateway.service.PermissionService;
import com.xiaoleilu.hutool.collection.CollectionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by czx on 2018/4/24.
 */
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        //ele-admin options 跨域配置，现在处理是通过前端配置代理，不使用这种方式，存在风险
//        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
//            return true;
//        }
        Object principal = authentication.getPrincipal();
        List<SimpleGrantedAuthority> grantedAuthorityList = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
        boolean hasPermission = false;

        if (principal != null) {
            if (CollectionUtil.isEmpty(grantedAuthorityList)) {
                return hasPermission;
            }

           /* Set<MenuVO> urls = new HashSet<>();
            for (SimpleGrantedAuthority authority : grantedAuthorityList) {
                urls.addAll(menuService.findMenuByRole(authority.getAuthority()));
            }

            for (MenuVO menu : urls) {
                if (StringUtils.isNotEmpty(menu.getUrl()) && antPathMatcher.match(menu.getUrl(), request.getRequestURI()) && request.getMethod().equalsIgnoreCase(menu.getMethod())) {
                    hasPermission = true;
                    break;
                }
            }*/
            hasPermission = true;
        }
        return hasPermission;
    }
}
