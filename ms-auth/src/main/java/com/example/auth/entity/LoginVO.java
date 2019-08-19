package com.example.auth.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author czx
 * @title: LoginVO
 * @projectName ms
 * @description: TODO
 * @date 2019/8/1916:10
 */
@Data
public class LoginVO implements Serializable {
    private String username;
    private String password;
    private String grant_type;
    private String scope;
    private String code;

    /**
     * 扩展字段
     **/
    private String attr;
}
