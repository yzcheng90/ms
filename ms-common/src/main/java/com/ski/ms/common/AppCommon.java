package com.ski.ms.common;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 多数据源
 * @SpringBootApplication(exclude = {
 *		DataSourceAutoConfiguration.class
 * })
 *
 * 首先要将spring boot自带的DataSourceAutoConfiguration禁掉，
 * 因为它会读取application.properties文件的spring.datasource.
 * 属性并自动配置单数据源。在@SpringBootApplication注解中添加exclude属性即可
 */
@Slf4j
@EnableDiscoveryClient
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@ComponentScan(basePackages = {"com.ski.ms.common","com.ski.ms.lib"})
public class AppCommon
{
    public static void main(String[] args)
    {
        SpringApplication.run(AppCommon.class, args);
        log.info("================AppCommon公共服务启动成功==================");
    }
}
