package com.example.common.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * 登录日志
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2019-08-15 16:25:10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysLoginLogVO implements Serializable {
    private static final long serialVersionUID = 1L;

    //名称
    private String log_name;
    //接口URL
    private String request_uri;
    //请求HOST
    private String request_host;
    //请求IP
    private String request_ip;
    //请求IP详细信息
    private String request_detail;
    //请求方式
    private String request_method;
    //用户代理
    private String user_agent;
    //登录模式
    private String grant_type;
    //用户名
    private String user_name;
    //客户端ID
    private String client_id;
    //token
    private String token;
    //登录时间
    @DateTimeFormat
    private LocalDateTime create_time;

}
