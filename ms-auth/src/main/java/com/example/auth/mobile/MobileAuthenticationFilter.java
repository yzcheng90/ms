package com.example.auth.mobile;

import com.example.common.core.constants.SecurityConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 除了oauth2 自带的4种认证模式，想要实现其他的认证模式，就继承 AbstractAuthenticationProcessingFilter 重写 attemptAuthentication 方法
 * oauth2 自带的4种模式是 ClientCredentialsTokenEndpointFilter 类处理，该类也是继承 AbstractAuthenticationProcessingFilter 重写attemptAuthentication方法
 * 继承 AbstractAuthenticationProcessingFilter 在构造的时候要传入 其他认证模式的 url
 * 如：本例子手机号登录验证
 */
public class MobileAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	private static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";
	@Getter
	@Setter
	private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;
	@Getter
	@Setter
	private boolean postOnly = true;
	@Getter
	@Setter
	private AuthenticationEntryPoint authenticationEntryPoint;


	public MobileAuthenticationFilter() {
		super(new AntPathRequestMatcher(SecurityConstants.MOBILE_TOKEN_URL, "POST"));
	}

	@Override
	@SneakyThrows
	public Authentication attemptAuthentication(HttpServletRequest request,HttpServletResponse response) {
		/**
		 * 只能POST登录
		 */
		if (postOnly && !request.getMethod().equals(HttpMethod.POST.name())) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String mobile = request.getParameter(mobileParameter);
		if (mobile == null) {
			mobile = "";
		}
		mobile = mobile.trim();

		/**
		 * 用手机号码生成token
		 */
		MobileAuthenticationToken mobileAuthenticationToken = new MobileAuthenticationToken(mobile);
		mobileAuthenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));


		Authentication authResult = null;
		try {
			authResult = this.getAuthenticationManager().authenticate(mobileAuthenticationToken);

			logger.debug("Authentication success: " + authResult);
			SecurityContextHolder.getContext().setAuthentication(authResult);

		} catch (Exception failed) {
			SecurityContextHolder.clearContext();
			logger.debug("Authentication request failed: " + failed);
			try {
				authenticationEntryPoint.commence(request, response,new UsernameNotFoundException(failed.getMessage(), failed));
			} catch (Exception e) {
				logger.error("authenticationEntryPoint handle error:{}", failed);
			}
		}

		return authResult;
	}

}

