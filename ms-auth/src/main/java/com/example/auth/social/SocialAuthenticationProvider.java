package com.example.auth.social;

import com.example.auth.compoent.UserAuthenticationChecks;
import com.example.auth.service.AuthenticationSocialUserService;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 * 手机登录校验逻辑
 * 验证码登录、第三方应用登录
 */
@Slf4j
public class SocialAuthenticationProvider implements AuthenticationProvider {
	private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	private UserDetailsChecker detailsChecker = new UserAuthenticationChecks();

	@Setter
	private AuthenticationSocialUserService authenticationSocialUserService;

	@Override
	@SneakyThrows
	public Authentication authenticate(Authentication authentication) {
		SocialAuthenticationToken socialAuthenticationToken = (SocialAuthenticationToken) authentication;

		String principal = socialAuthenticationToken.getPrincipal().toString();
		UserDetails userDetails = authenticationSocialUserService.getSocialUserInfo(principal);
		if (userDetails == null) {
			log.debug("Authentication failed: no credentials provided");

			throw new BadCredentialsException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.noopBindAccount",
					"Noop Bind Account"));
		}

		detailsChecker.check(userDetails);

		SocialAuthenticationToken authenticationToken = new SocialAuthenticationToken(userDetails, userDetails.getAuthorities());
		authenticationToken.setDetails(socialAuthenticationToken.getDetails());
		return authenticationToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return SocialAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
