package com.ski.ms.main.hystrix;

import com.ski.ms.lib.entity.UserEntity;
import com.ski.ms.main.hystrix.fallback.UserServiceFallbackImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * name ：调用公共服务
 * fallback ： 溶错处理
 * Created by czx on 2018/4/23.
 */
@FeignClient(name = "ms-common", fallback = UserServiceFallbackImpl.class)
public interface UserService {
    /**
     * 通过用户名查询用户、角色信息
     * @param username 用户名
     * @return UserEntity
     */
    @GetMapping("/user/findUserByUsername/{username}")
    UserEntity findUserByUsername(@PathVariable("username") String username);

    /**
     * 通过手机号查询用户、角色信息
     * @param mobile 手机号
     * @return UserEntity
     */
    @GetMapping("/user/findUserByMobile/{mobile}")
    UserEntity findUserByMobile(@PathVariable("mobile") String mobile);

}
