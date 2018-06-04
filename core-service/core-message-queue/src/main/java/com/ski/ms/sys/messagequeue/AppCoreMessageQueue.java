package com.ski.ms.sys.messagequeue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class AppCoreMessageQueue
{

    public static void main(String[] args)
    {
        SpringApplication.run(AppCoreMessageQueue.class, args);
        log.info("================AppCoreMessageQueue消息列队服务启动成功==================");
    }
}
