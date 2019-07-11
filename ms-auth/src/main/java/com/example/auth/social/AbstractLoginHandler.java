package com.example.auth.social;

import com.example.common.resource.entity.UserInfo;

/**
 * @Description //TODO 默认抽象实现$
 * @Date 21:30
 * @Author yzcheng90@qq.com
 **/
public abstract class AbstractLoginHandler implements LoginHandler{

    /**
     * 数据检查
     * @param loginStr 通过用户传入获取唯一标识
     * @return 默认通过
     */
    @Override
    public Boolean check(String loginStr) {
        return true;
    }

    /**
     * 登录
     * @param loginStr 登录参数
     * @return
     */
    @Override
    public UserInfo login(String loginStr) {
        if (!check(loginStr)) {
            return null;
        }

        String identify = identify(loginStr);
        return info(identify);
    }
}
