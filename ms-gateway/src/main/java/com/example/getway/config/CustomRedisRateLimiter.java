package com.example.getway.config;

import com.example.common.core.constants.CommonConstants;
import com.example.common.core.entity.RateLimiterLevel;
import com.example.common.core.entity.RateLimiterVO;
import com.example.common.gateway.inteface.LimiterLevelResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.validation.constraints.Min;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author czx
 * @title: CustomRedisRateLimiter
 * @projectName demo1
 * @description: TODO 自己定义限流
 * @date 2019/7/515:15
 */
@Slf4j
public class CustomRedisRateLimiter extends AbstractRateLimiter<CustomRedisRateLimiter.Config> implements ApplicationContextAware {

    public static final String CONFIGURATION_PROPERTY_NAME = "redis-rate-limiter";
    public static final String REDIS_SCRIPT_NAME = "redisRequestRateLimiterScript";
    public static final String REMAINING_HEADER = "X-RateLimit-Remaining";
    public static final String REPLENISH_RATE_HEADER = "X-RateLimit-Replenish-Rate";
    public static final String BURST_CAPACITY_HEADER = "X-RateLimit-Burst-Capacity";

    private ReactiveRedisTemplate<String, String> redisTemplate;
    private RedisScript<List<Long>> script;
    private AtomicBoolean initialized = new AtomicBoolean(false);

    private String remainingHeader = REMAINING_HEADER;

    /** The name of the header that returns the replenish rate configuration. */
    private String replenishRateHeader = REPLENISH_RATE_HEADER;

    /** The name of the header that returns the burst capacity configuration. */
    private String burstCapacityHeader = BURST_CAPACITY_HEADER;

    @Autowired
    private LimiterLevelResolver limiterLevelResolver;

    public CustomRedisRateLimiter(ReactiveRedisTemplate<String, String> redisTemplate,RedisScript<List<Long>> script, Validator validator) {
        super(Config.class , CONFIGURATION_PROPERTY_NAME , validator);
        this.redisTemplate = redisTemplate;
        this.script = script;
        initialized.compareAndSet(false,true);
    }

    @Override
    public Mono<RateLimiter.Response> isAllowed(String routeId, String id) {
        if (!this.initialized.get()) {
            throw new IllegalStateException("RedisRateLimiter is not initialized");
        }
        if (ObjectUtils.isEmpty(limiterLevelResolver) ){
            throw new IllegalArgumentException("No Configuration found for route " + routeId);
        }
        RateLimiterLevel rateLimiterLevel = limiterLevelResolver.get();
        // How many requests per second do you want a user to be allowed to do?
        int replenishRate = rateLimiterLevel
                .getLevels()
                .stream()
                .filter(rateLimiterVO -> rateLimiterVO.getLevel().equals(id))
                .findFirst()
                .map(RateLimiterVO::getReplenishRate)
                .orElse(CommonConstants.DEFAULT_LIMIT_LEVEL);
        // How much bursting do you want to allow?
        int burstCapacity = rateLimiterLevel
                .getLevels()
                .stream()
                .filter(rateLimiterVO -> rateLimiterVO.getLevel().equals(id))
                .findFirst()
                .map(RateLimiterVO::getBurstCapacity)
                .orElse(CommonConstants.DEFAULT_LIMIT_LEVEL);
        try {
            List<String> keys = getKeys(id);
            long limitTime = getTime(rateLimiterLevel
                    .getLevels()
                    .stream()
                    .filter(rateLimiterVO -> rateLimiterVO.getLevel().equals(id))
                    .findFirst()
                    .map(RateLimiterVO::getLimitType)
                    .orElse(CommonConstants.DEFAULT_LIMIT_TYPE));
            List<String> scriptArgs = Arrays.asList(replenishRate + "", burstCapacity + "",limitTime + "", "1");
            Flux<List<Long>> flux = this.redisTemplate.execute(this.script, keys, scriptArgs);

            return flux.onErrorResume(throwable -> Flux.just(Arrays.asList(1L, -1L)))
                    .reduce(new ArrayList<Long>(), (longs, l) -> {
                        longs.addAll(l);
                        return longs;
                    }) .map(results -> {
                        boolean allowed = results.get(0) == 1L;
                        Long tokensLeft = results.get(1);
                        RateLimiter.Response response = new RateLimiter.Response(allowed, getHeaders(replenishRate , burstCapacity , tokensLeft));
                        return response;
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Mono.just(new RateLimiter.Response(true, getHeaders(replenishRate , burstCapacity , -1L)));
    }

    public HashMap<String, String> getHeaders(Integer replenishRate, Integer burstCapacity , Long tokensLeft) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(this.remainingHeader, tokensLeft.toString());
        headers.put(this.replenishRateHeader, String.valueOf(replenishRate));
        headers.put(this.burstCapacityHeader, String.valueOf(burstCapacity));
        return headers;
    }

    static List<String> getKeys(String id) {
        // use `{}` around keys to use Redis Key hash tags
        // this allows for using redis cluster

        // Make a unique key per user.
        String prefix = "request_user_rate_limiter.{" + id;

        // You need two Redis keys for Token Bucket.
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }

    /**
     * @Date 14:52 2019/7/15
     * @Param [type] 1:秒，2:分钟，3:小时，4:天
     * @return long
     **/
    public long getTime(int type){
        long time = Instant.now().getEpochSecond();
        switch (type){
            case 1:
                break;
            case 2:
                time = time / (1000 * 60);
                break;
            case 3:
                time = time / (1000 * 60 * 60);
                break;
            case 4:
                time = time / (1000 * 60 * 60 * 24);
                break;
        }
        return time;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (initialized.compareAndSet(false, true)) {
            this.redisTemplate = applicationContext.getBean("stringReactiveRedisTemplate",ReactiveRedisTemplate.class);
            this.script = applicationContext.getBean(REDIS_SCRIPT_NAME, RedisScript.class);
            if (applicationContext.getBeanNamesForType(Validator.class).length > 0) {
                this.setValidator(applicationContext.getBean(Validator.class));
            }
        }
    }


    @Validated
    public static class Config{
        @Min(1)
        private int replenishRate;
        @Min(1)
        private int burstCapacity = 1;

        public int getReplenishRate() {
            return replenishRate;
        }

        public Config setReplenishRate(int replenishRate) {
            this.replenishRate = replenishRate;
            return this;
        }

        public int getBurstCapacity() {
            return burstCapacity;
        }

        public Config setBurstCapacity(int burstCapacity) {
            this.burstCapacity = burstCapacity;
            return this;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "replenishRate=" + replenishRate +
                    ", burstCapacity=" + burstCapacity +
                    '}';
        }
    }

}
