package com.ski.ms.common.common.config;

import com.ski.ms.lib.constant.MqQueueConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbit初始化配置
 */
@Configuration
public class RabbitConfig {
    /**
     * 初始化 log队列
     *
     * @return
     */
    @Bean
    public Queue initLogQueue() {
        return new Queue(MqQueueConstant.LOG_QUEUE);
    }

    /**
     * 初始化 手机验证码通道
     *
     * @return
     */
    @Bean
    public Queue initMobileCodeQueue() {
        return new Queue(MqQueueConstant.MOBILE_CODE_QUEUE);
    }

    /**
     * 初始化服务状态改变队列
     *
     * @return
     */
    @Bean
    public Queue initServiceStatusChangeQueue() {
        return new Queue(MqQueueConstant.SERVICE_STATUS_CHANGE);
    }

    @Bean
    public Queue initZipkinQueue() {
        return new Queue(MqQueueConstant.ZIPKIN_NAME_QUEUE);
    }
}
