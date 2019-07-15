package com.example.common.core.constants;

/**
 * 认证专用常量类
 */
public interface SecurityConstants {

    /**
     * 手机号获取token URL
     */
    String MOBILE_TOKEN_URL = "/mobile/token/*";

    /**
     * {bcrypt} 加密的特征码
     */
    String BCRYPT = "{bcrypt}";

    /**
     * token key 前缀
     */
    String MS_OAUTH_PREFIX = "ms_oauth:";

    /**
     * token key
     */
    String ACCESS = "access:";

    /**
     * 用户ID
     */
    String USER_ID = "user_id";

    /**
     * 用户名
     */
    String USER_NAME = "username";

    /**
     * 限流等级
     */
    String LIMIT_LEVEL = "limit_level";

    /**
     * redis key oauth 客户端信息
     */
    String CLIENT_DETAILS_KEY = "ms_oauth:client:details";

    String CACHE_USER_DETAILS = "cache_user_details";

    /**
     * 查询客户端sql
     */
    String CLIENT_QUERY_SQL = "select client_id, CONCAT('{noop}',client_secret) as client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove from sys_oauth_client_details";

    /**
     * 默认的查询语句
     */
    String DEFAULT_FIND_STATEMENT = CLIENT_QUERY_SQL + " order by client_id";

    /**
     * 按条件client_id 查询
     */
    String DEFAULT_SELECT_STATEMENT = CLIENT_QUERY_SQL + " where client_id = ?";

    /**
     * 资源服务器默认bean名称
     */
    String RESOURCE_SERVER_CONFIGURER = "resourceServerConfigurerAdapter";

    /**
     * 客户端模式
     */
    String CLIENT_CREDENTIALS = "client_credentials";
}
