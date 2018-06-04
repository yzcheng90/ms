package com.ski.ms.sys.messagequeue.listener;

import com.ski.ms.lib.constant.MqQueueConstant;
import com.ski.ms.lib.utils.MobileMsgTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 监听服务状态改变发送请求
 */
@Slf4j
@Component
@RabbitListener(queues = MqQueueConstant.SERVICE_STATUS_CHANGE)
public class ServiceChangeReceiveListener {
    @RabbitHandler
    public void receive(MobileMsgTemplate mobileMsgTemplate) {
        long startTime = System.currentTimeMillis();
        log.info("消息中心接收到短信发送请求-> 手机号：{} -> 验证码: {} ", mobileMsgTemplate.getMobile(), mobileMsgTemplate.getText());
        long useTime = System.currentTimeMillis() - startTime;
        log.info("调用 {} 短信网关处理完毕，耗时 {}毫秒", mobileMsgTemplate.getType(), useTime);
    }
}
