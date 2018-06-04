package com.ski.ms.sys.serviceregister;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@Slf4j
@EnableEurekaServer
@SpringBootApplication
public class AppCoreServiceRegister
{

    public static void main(String[] args)
    {
        SpringApplication.run(AppCoreServiceRegister.class, args);
        log.info("================AppCoreServiceRegister服务注册中心启动成功==================");
    }
}
