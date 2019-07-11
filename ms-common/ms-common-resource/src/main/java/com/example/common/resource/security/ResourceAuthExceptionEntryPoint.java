package com.example.common.resource.security;

import cn.hutool.http.HttpStatus;
import com.example.common.core.entity.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 客户端异常处理
 * 1. 可以根据 AuthenticationException 不同细化异常处理
 */
@Slf4j
@Component
@AllArgsConstructor
public class ResourceAuthExceptionEntryPoint implements AuthenticationEntryPoint {
	//json 转换包
	private final ObjectMapper objectMapper;

	@Override
	@SneakyThrows
	public void commence(HttpServletRequest request, HttpServletResponse response,AuthenticationException authException) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		R<String> result = new R<>();
		result.setCode(HttpStatus.HTTP_INTERNAL_ERROR);
		if (authException != null) {
			if(authException.getCause() instanceof InvalidTokenException){
				result.setCode(HttpStatus.HTTP_UNAUTHORIZED);
				result.setMessage("无效Token");
				result.setData(authException.getMessage());
			}else {
				result.setMessage(authException.getMessage());
				result.setData(authException.getMessage());
			}
		}
		response.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
		PrintWriter printWriter = response.getWriter();
		printWriter.append(objectMapper.writeValueAsString(result));
	}
}
