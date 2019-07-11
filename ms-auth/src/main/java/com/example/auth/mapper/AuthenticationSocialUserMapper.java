package com.example.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.common.resource.entity.SysSocialDetails;
import com.example.common.resource.entity.UserInfo;

public interface AuthenticationSocialUserMapper extends BaseMapper<SysSocialDetails> {

    UserInfo getSocialUserInfo(String key);
}
