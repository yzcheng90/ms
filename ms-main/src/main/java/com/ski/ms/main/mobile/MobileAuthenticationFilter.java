package com.ski.ms.main.mobile;

import com.ski.ms.lib.constant.SecurityConstant;
import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 手机号登录验证filter
 * Created by czx on 2018/4/27.
 */
@Data
public class MobileAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";

    private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;
    private boolean postOnly = true;

    public MobileAuthenticationFilter() {
        super(new AntPathRequestMatcher(SecurityConstant.MOBILE_TOKEN_URL, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }


        String mobile = request.getParameter(mobileParameter);
        if (mobile == null) {
            mobile = "";
        }
        mobile = mobile.trim();
        MobileAuthenticationToken mobileAuthenticationToken = new MobileAuthenticationToken(mobile);
        mobileAuthenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));

        return this.getAuthenticationManager().authenticate(mobileAuthenticationToken);
    }
}

