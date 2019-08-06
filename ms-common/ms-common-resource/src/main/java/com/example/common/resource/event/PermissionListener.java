package com.example.common.resource.event;

import com.example.common.resource.remote.RemotePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * @Description //TODO $事件监听
 * @Date 23:48
 * @Author yzcheng90@qq.com
 **/
@Slf4j
public class PermissionListener {

    @Autowired
    private RemotePermissionService remotePermissionService;

    @Async
    @Order
    @EventListener(PermissionEvent.class)
    public void updatePermission(PermissionEvent event){
        log.info("=======调用远程服务更新权限=======");
        remotePermissionService.update(event.getList());
    }
}
