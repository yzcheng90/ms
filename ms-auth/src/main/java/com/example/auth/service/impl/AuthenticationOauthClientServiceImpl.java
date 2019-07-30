package com.example.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.auth.mapper.AuthenticationOauthClientMapper;
import com.example.auth.service.AuthenticationOauthClientService;
import com.example.common.user.entity.SysOauthClientDetails;
import org.springframework.stereotype.Service;

/**
 * @Description //TODO
 * @Date 21:07
 * @Author czx
 **/
@Service
public class AuthenticationOauthClientServiceImpl extends ServiceImpl<AuthenticationOauthClientMapper,SysOauthClientDetails> implements AuthenticationOauthClientService {

}
