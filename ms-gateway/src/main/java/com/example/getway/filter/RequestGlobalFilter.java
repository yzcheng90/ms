package com.example.getway.filter;

import com.example.common.cache.component.RedisUUID;
import com.example.common.core.constants.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description //TODO 全局过滤
 * @Date 22:20
 * @Author yzcheng90@qq.com
 **/
@Component
public class RequestGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisUUID redisUUID;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String secretKey = redisUUID.create(SecurityConstants.SECRET_KEY);
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(httpHeaders -> httpHeaders.remove(SecurityConstants.SECRET_KEY))
                .build();
        ServerHttpRequest newRequest = request.mutate().header(SecurityConstants.SECRET_KEY,secretKey).build();
        return chain.filter(exchange.mutate().request(newRequest.mutate().build()).build());
    }

    @Override
    public int getOrder() {
        return -999;
    }
}
