package com.ski.ms.sys.gateway.service;

import com.netflix.zuul.context.RequestContext;

/**
 * Created by czx on 2018/4/24.
 */
public interface LogSendService {
    /**
     * 往消息通道发消息
     *
     * @param requestContext requestContext
     */
    void send(RequestContext requestContext);
}
