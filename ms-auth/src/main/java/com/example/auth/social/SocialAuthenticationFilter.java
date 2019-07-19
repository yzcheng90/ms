package com.example.auth.social;

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
 * 如：本例子第三方登录验证
 */
public class SocialAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	private static final String SPRING_SECURITY_FORM_SOCIAL_KEY = "social";
	@Getter
	@Setter
	private String socialParameter = SPRING_SECURITY_FORM_SOCIAL_KEY;
	@Getter
	@Setter
	private boolean postOnly = true;
	@Getter
	@Setter
	private AuthenticationEntryPoint authenticationEntryPoint;


	public SocialAuthenticationFilter() {
		super(new AntPathRequestMatcher(SecurityConstants.SOCIAL_TOKEN_URL, "POST"));
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

		String social = request.getParameter(socialParameter);
		if (social == null) {
			social = "";
		}
		social = social.trim();
		SocialAuthenticationToken socialAuthenticationToken = new SocialAuthenticationToken(social);
		socialAuthenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));


		Authentication authResult = null;
		try {
			authResult = this.getAuthenticationManager().authenticate(socialAuthenticationToken);

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

