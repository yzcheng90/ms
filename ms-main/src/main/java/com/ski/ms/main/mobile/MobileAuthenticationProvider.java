package com.ski.ms.main.mobile;

import com.ski.ms.lib.entity.UserEntity;
import com.ski.ms.main.hystrix.UserService;
import com.ski.ms.main.utils.UserDetailsImpl;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 手机号登录校验逻辑
 * Created by czx on 2018/4/27.
 */
@Data
public class MobileAuthenticationProvider implements AuthenticationProvider {
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobileAuthenticationToken mobileAuthenticationToken = (MobileAuthenticationToken) authentication;
        UserEntity userEntity = userService.findUserByMobile((String) mobileAuthenticationToken.getPrincipal());

        if (userEntity == null) {
            throw new UsernameNotFoundException("手机号不存在:" + mobileAuthenticationToken.getPrincipal());
        }

        //封装security 的对象
        UserDetailsImpl userDetails = new UserDetailsImpl(userEntity);

        MobileAuthenticationToken authenticationToken = new MobileAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationToken.setDetails(mobileAuthenticationToken.getDetails());
        return authenticationToken;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
