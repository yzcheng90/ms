package com.example.common.interceptor.config;

import com.example.common.interceptor.interceptor.GlobalInterceptor;
import com.example.common.resource.config.AuthIgnoreConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description //TODO $
 * @Date 21:46
 * @Author yzcheng90@qq.com
 **/
@Configurable
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private WebApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        GlobalInterceptor interceptor = new GlobalInterceptor();
        interceptor.setRedisTemplate(redisTemplate());
        interceptor.setAuthIgnoreConfig(applicationContext.getBean(AuthIgnoreConfig.class));
        registry.addInterceptor(interceptor).addPathPatterns("/**");
    }
}
