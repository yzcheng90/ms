package com.example.getway.filter;

import cn.hutool.crypto.SecureUtil;
import com.example.common.core.constants.SecurityConstants;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Description //TODO 全局过滤
 * @Date 22:20
 * @Author yzcheng90@qq.com
 **/
@Component
public class RequestGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisTemplate redisTemplate;

    // 过期时间
    public final static long expiration = 1000 * 60 * 5;

    // 过期时间
    public final static long lastTime = 1000 * 60;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String secretKey;
        if(redisTemplate.hasKey(SecurityConstants.SECRET_KEY)){
            if(redisTemplate.boundHashOps(SecurityConstants.SECRET_KEY).getExpire() < lastTime){
                redisTemplate.opsForValue().set(SecurityConstants.SECRET_KEY,SecureUtil.md5(UUID.randomUUID().toString()),expiration,TimeUnit.SECONDS);
            }
            secretKey = (String) redisTemplate.opsForValue().get(SecurityConstants.SECRET_KEY);
        }else{
            secretKey = SecureUtil.md5(UUID.randomUUID().toString());
            redisTemplate.opsForValue().set(SecurityConstants.SECRET_KEY,secretKey,expiration,TimeUnit.SECONDS);
        }
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(httpHeaders -> httpHeaders.remove(SecurityConstants.SECRET_KEY))
                .build();
        ServerHttpRequest newRequest = request.mutate().header(SecurityConstants.SECRET_KEY,secretKey).build();
        return chain.filter(exchange.mutate()
                .request(newRequest.mutate().build())
                .build());
    }

    @Override
    public int getOrder() {
        return -999;
    }
}
