package com.example.common.core.constants;

/**
 * 公用常量类
 */
public interface CommonConstants {

    /**
     * 删除
     */
    String STATUS_DEL = "1";
    /**
     * 正常
     */
    String STATUS_NORMAL = "0";

    /**
     * 锁定
     */
    String STATUS_LOCK = "9";

    /**
     * 认证头
     */
    String AUTHORIZATION = "Authorization";

    /**
     * redis 认证用户信息
     */
    String AUTH_USER = "auth_user:";

    /**
     * 默认限流等级
     */
    String DEFAULT_LEVEL = "1";

    /**
     * 默认限流 流速 与 桶大小
     */
    int DEFAULT_LIMIT_LEVEL = 10;

    /**
     * 默认单位 1:秒，2:分钟，3:小时，4:天
     */
    int DEFAULT_LIMIT_TYPE = 1;

    /**
     * 前缀
     */
    String PREFIX = " ";

    /**
     * 限流缓存key
     */
    String REDIS_LIMIT_KEY = "ms_redis_limit_key:";


}
