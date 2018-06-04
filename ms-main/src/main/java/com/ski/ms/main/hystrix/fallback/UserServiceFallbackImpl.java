package com.ski.ms.main.hystrix.fallback;

import com.ski.ms.lib.entity.UserEntity;
import com.ski.ms.main.hystrix.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 用户服务的fallback
 * Created by czx on 2018/4/23.
 */
@Service
public class UserServiceFallbackImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserEntity findUserByUsername(String username) {
        logger.error("调用{}异常:{}", "findUserByUsername", username);
        return null;
    }

    /**
     * 通过手机号查询用户、角色信息
     * @param mobile 手机号
     * @return UserEntity
     */
    @Override
    public UserEntity findUserByMobile(String mobile) {
        logger.error("调用{}异常:{}", "通过手机号查询用户", mobile);
        return null;
    }

}
