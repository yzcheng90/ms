package com.example.auth.compoent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 * 用户认证信息检查
 */
@Slf4j
public class UserAuthenticationChecks implements UserDetailsChecker {
	private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	@Override
	public void check(UserDetails user) {
		if (!user.isAccountNonLocked()) {
			log.debug("User account is locked");
			throw new LockedException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked","User account is locked"));
		}
		if (!user.isEnabled()) {
			log.debug("User account is disabled");
			throw new DisabledException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled","User is disabled"));
		}
		if (!user.isAccountNonExpired()) {
			log.debug("User account is expired");
			throw new AccountExpiredException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired","User account has expired"));
		}
	}
}
