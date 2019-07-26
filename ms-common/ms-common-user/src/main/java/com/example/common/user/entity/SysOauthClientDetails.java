package com.example.common.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author czx
 * @title: SysOauthClientDetails
 * @projectName ms
 * @description: TODO 客户端管理  字段解释 http://www.andaily.com/spring-oauth-server/db_table_description.html
 * @date 2019/7/2613:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oauth_client_details")
public class SysOauthClientDetails extends Model<SysOauthClientDetails> {

    @TableId
    private String clientId;
    private String resourceIds;
    private String clientSecret;
    private String scope;
    private String authorizedGrantTypes;
    private String webServerRedirectUri;
    private String authorities;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String additionalInformation;
    private String autoapprove;
}
