package com.example.admin.listener;

import cn.hutool.core.util.ObjectUtil;
import com.example.admin.entity.SysLoginLog;
import com.example.admin.service.SysLoginLogService;
import com.example.admin.service.SysPermissionService;
import com.example.common.core.entity.PermissionEntityVO;
import com.example.common.core.entity.SysLoginLogVO;
import com.example.common.rabbitmq.constants.RabbitMQConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author czx
 * @title: LoginLogListener
 * @projectName ms
 * @description: TODO 登录日志消息监听
 * @date 2019/8/712:23
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitMQConstants.LOGIN_LOG_QUEUE)
@AllArgsConstructor
public class LoginLogListener {

    private final SysLoginLogService sysLoginLogService;

    /**
     * 消息消费
     */
    @RabbitHandler
    public void recieved(SysLoginLogVO vo) {
        log.info("===========LoginLogListener收到消息=============");
        if(ObjectUtil.isNotNull(vo)){
            vo.setLog_name("用户登录");
            vo.setCreate_time(new Date());
            SysLoginLog sysLoginLog = new SysLoginLog();
            BeanUtils.copyProperties(vo,sysLoginLog);
            sysLoginLogService.save(sysLoginLog);
        }
    }

}
