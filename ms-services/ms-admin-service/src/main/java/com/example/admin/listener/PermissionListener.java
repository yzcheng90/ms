package com.example.admin.listener;

import cn.hutool.core.util.ObjectUtil;
import com.example.admin.service.SysPermissionService;
import com.example.common.core.entity.PermissionEntityVO;
import com.example.common.rabbitmq.constants.RabbitMQConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author czx
 * @title: PermissionListener
 * @projectName ms
 * @description: TODO 权限消息监听
 * @date 2019/8/712:23
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitMQConstants.PERMISSION_QUEUE)
@AllArgsConstructor
public class PermissionListener {

    private final SysPermissionService sysPermissionService;

    /**
     * 消息消费
     */
    @RabbitHandler
    public void recieved(List<PermissionEntityVO> list) {
        log.info("===========PermissionListener收到消息=============");
        if(ObjectUtil.isNotNull(list)){
            sysPermissionService.updateSysPermission(list);
        }
    }

}
