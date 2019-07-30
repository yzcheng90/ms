package com.example.auth.handler;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.auth.mapper.SysRoleMapper;
import com.example.auth.mapper.SysUserRoleMapper;
import com.example.auth.service.AuthenticationUserService;
import com.example.auth.entity.UserInfo;
import com.example.common.user.entity.SysRole;
import com.example.common.user.entity.SysUser;
import com.example.common.user.entity.SysUserRole;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 手机号码登录处理
 */
@Slf4j
@Component("SMS")
@AllArgsConstructor
public class SmsLoginHandler extends AbstractLoginHandler {
	private final AuthenticationUserService authenticationUserService;
	private final SysUserRoleMapper sysUserRoleMapper;
	private final SysRoleMapper sysRoleMapper;

	/**
	 * 验证码登录传入为手机号
	 * 不用不处理
	 *
	 * @param mobile
	 * @return
	 */
	@Override
	public String identify(String mobile) {
		return mobile;
	}

	/**
	 * 通过mobile 获取用户信息
	 *
	 * @param identify
	 * @return
	 */
	@Override
	public UserInfo info(String identify) {
		SysUser sysUser = authenticationUserService
				.getOne(Wrappers.<SysUser>query()
						.lambda().eq(SysUser::getPhone, identify));
		List<Long> roleIds = sysUserRoleMapper
				.selectList(Wrappers.<SysUserRole>query().lambda().eq(SysUserRole::getUserId,sysUser.getUserId()))
				.stream()
				.map(SysUserRole::getRoleId)
				.collect(Collectors.toList());
		if(CollUtil.isNotEmpty(roleIds)){
			List<String> roleCodes = sysRoleMapper.selectList(Wrappers.<SysRole>query().lambda().in(SysRole::getRoleId,roleIds))
					.stream()
					.map(SysRole::getRoleCode)
					.collect(Collectors.toList());
			sysUser.setRoleCode(roleCodes);
		}
		if (sysUser == null) {
			log.info("手机号未注册:{}", identify);
			throw new OAuth2Exception("手机号未注册:"+identify);
		}
		return UserInfo.builder().user(sysUser).build();
	}
}
