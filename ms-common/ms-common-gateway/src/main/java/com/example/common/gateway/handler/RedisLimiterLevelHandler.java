package com.example.common.gateway.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.core.constants.CommonConstants;
import com.example.common.core.entity.RateLimiterLevel;
import com.example.common.gateway.inteface.LimiterLevelResolver;
import com.example.common.gateway.serialization.RedisTokenStoreSerializationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author czx
 * @title: RedisLimiterLevelHandler
 * @projectName ms
 * @description: TODO redis 管理 limiter
 * @date 2019/7/1510:50
 */
public class RedisLimiterLevelHandler implements LimiterLevelResolver {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisTokenStoreSerializationStrategy redisTokenStoreSerializationStrategy;

    @Override
    public void save(RateLimiterLevel limiterLevel) {
        byte[] key = redisTokenStoreSerializationStrategy.serialize(CommonConstants.REDIS_LIMIT_KEY);
        byte[] value = redisTokenStoreSerializationStrategy.serialize(limiterLevel);
        try{
            redisTemplate.getConnectionFactory().getConnection().openPipeline();
            redisTemplate.getConnectionFactory().getConnection().set(key, value);
            redisTemplate.getConnectionFactory().getConnection().closePipeline();
        }finally {
            redisTemplate.getConnectionFactory().getConnection().close();
        }
    }

    @Override
    public RateLimiterLevel get() {
        byte[] key = redisTokenStoreSerializationStrategy.serialize(CommonConstants.REDIS_LIMIT_KEY);
        byte[] value = redisTemplate.getConnectionFactory().getConnection().get(key);
        RateLimiterLevel rateLimiterLevel = redisTokenStoreSerializationStrategy.deserialize(value,RateLimiterLevel.class);
        if(ObjectUtil.isNull(value) ||  ObjectUtil.isNull(rateLimiterLevel) || CollUtil.isEmpty(rateLimiterLevel.getLevels())){
            rateLimiterLevel = new RateLimiterLevel();
            Map<String,Integer[]> map = new HashMap<>();
            map.put(CommonConstants.DEFAULT_LEVEL,new Integer[]{CommonConstants.DEFAULT_LIMIT_LEVEL,CommonConstants.DEFAULT_LIMIT_LEVEL,CommonConstants.DEFAULT_LIMIT_TYPE});
            rateLimiterLevel.setLevels(map);
        }
        return rateLimiterLevel;
    }
}
