package com.ski.ms.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableHystrix
@ComponentScan(basePackages = {"com.ski.ms.main", "com.ski.ms.lib"})
public class AppMain
{
    public static void main(String[] args)
    {
        SpringApplication.run(AppMain.class, args);
        log.info("================AppMain统一认证服务启动成功==================");
    }
}
