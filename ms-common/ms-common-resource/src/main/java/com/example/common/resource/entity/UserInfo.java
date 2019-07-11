package com.example.common.resource.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description //TODO $ 用户信息增强类
 * @Date 20:23
 * @Author yzcheng90@qq.com
 **/
@Data
@Builder
@Accessors(chain = true)
public class UserInfo implements Serializable {

    private SysUser user;
}
