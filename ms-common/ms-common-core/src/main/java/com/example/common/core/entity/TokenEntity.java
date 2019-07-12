package com.example.common.core.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author czx
 * @title: TokenEntity
 * @projectName ms
 * @description: TODO
 * @date 2019/7/1210:01
 */
@Data
public class TokenEntity {

    private String token;
    private Date expiration;
    private Integer userId;
    private String username;
    private String clientId;
    private String grantType;
    private Integer limitLevel;
}
