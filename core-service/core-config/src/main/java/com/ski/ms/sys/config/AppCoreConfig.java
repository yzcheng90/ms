package com.ski.ms.sys.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@Slf4j
@EnableDiscoveryClient
@EnableConfigServer
@SpringBootApplication
public class AppCoreConfig
{

    public static void main(String[] args)
    {
        SpringApplication.run(AppCoreConfig.class, args);
        log.info("================AppCoreConfig配置中心启动成功==================");
    }
}
