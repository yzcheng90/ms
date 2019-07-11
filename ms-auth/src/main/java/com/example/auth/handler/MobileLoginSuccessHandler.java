package com.example.auth.handler;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharsetUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 手机号登录成功，返回oauth token
 */
@Slf4j
@Builder
public class MobileLoginSuccessHandler implements AuthenticationSuccessHandler {
	private static final String BASIC_ = "Basic ";
	private ObjectMapper objectMapper;
	private PasswordEncoder passwordEncoder;
	private ClientDetailsService clientDetailsService;
	private AuthorizationServerTokenServices defaultAuthorizationServerTokenServices;

	/**
	 * 登录验证成功后回调这个方法.
	 * 调用spring security oauth API 生成 oAuth2AccessToken
	 *
	 * @param request        the request which caused the successful authentication
	 * @param response       the response
	 * @param authentication the <tt>Authentication</tt> object which was created during
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (header == null || !header.startsWith(BASIC_)) {
			throw new UnapprovedClientAuthenticationException("请求头中client信息为空");
		}

		try {
			String[] tokens = getClientDetails(header);
			assert tokens.length == 2;
			String clientId = tokens[0];

			ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

			//校验secret
			if (!passwordEncoder.matches(tokens[1], clientDetails.getClientSecret())) {
				throw new InvalidClientException("clientId信息错误");
			}

			TokenRequest tokenRequest = new TokenRequest(MapUtil.newHashMap(), clientId, clientDetails.getScope(), "mobile");

			//校验scope
			new DefaultOAuth2RequestValidator().validateScope(tokenRequest, clientDetails);
			OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
			OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
			OAuth2AccessToken oAuth2AccessToken = defaultAuthorizationServerTokenServices.createAccessToken(oAuth2Authentication);
			log.info("获取token 成功：{}", oAuth2AccessToken.getValue());

			response.setCharacterEncoding(CharsetUtil.UTF_8);
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			PrintWriter printWriter = response.getWriter();
			printWriter.append(objectMapper.writeValueAsString(oAuth2AccessToken));
		} catch (IOException e) {
			throw new BadCredentialsException("basic 信息错误");
		}

	}

	@SneakyThrows
	public String[] getClientDetails(String header){
		byte[] base64Token = header.substring(6).getBytes("UTF-8");
		byte[] decoded;
		try {
			decoded = Base64.decode(base64Token);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("客户端信息无效");
		}

		String token = new String(decoded, StandardCharsets.UTF_8);

		int index = token.indexOf(":");
		if (index == -1) {
			throw new RuntimeException("客户端信息无效");
		}
		return new String[]{token.substring(0, index), token.substring(index + 1)};
	}


}