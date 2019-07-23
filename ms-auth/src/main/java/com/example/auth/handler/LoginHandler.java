package com.example.auth.handler;


import com.example.auth.entity.UserInfo;

/**
 * 第三方登录接口
 */
public interface LoginHandler {

    /***
     * 数据合法性校验
     * @param loginStr 通过用户传入获取唯一标识
     * @return
     */
    Boolean check(String loginStr);

    /**
     * 通过用户传入获取唯一标识
     *
     * @param loginStr
     * @return
     */
    String identify(String loginStr);

    /**
     * 通过openId 获取用户信息
     *
     * @param identify
     * @return
     */
    UserInfo info(String identify);

    /**
     * 处理方法
     *
     * @param loginStr 登录参数
     * @return
     */
    UserInfo login(String loginStr);

}
