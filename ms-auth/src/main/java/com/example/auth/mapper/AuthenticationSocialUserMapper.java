package com.example.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auth.entity.UserInfo;
import com.example.common.user.entity.SysSocialDetails;

public interface AuthenticationSocialUserMapper extends BaseMapper<SysSocialDetails> {

    UserInfo getSocialUserInfo(String key);
}
