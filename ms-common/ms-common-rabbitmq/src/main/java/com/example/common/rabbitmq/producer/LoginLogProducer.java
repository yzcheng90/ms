package com.example.common.rabbitmq.producer;

import com.example.common.core.entity.SysLoginLogVO;
import com.example.common.rabbitmq.constants.RabbitMQConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author czx
 * @title: LoginLogProducer
 * @projectName ms
 * @description: TODO 登录日志
 * @date 2019/8/1516:51
 */
@Slf4j
@Component
public class LoginLogProducer {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(SysLoginLogVO vo){
        log.info("=============LoginLogProducer消息发送成功=================");
        rabbitTemplate.convertAndSend(RabbitMQConstants.LOGIN_LOG_QUEUE, vo);
    }
}
