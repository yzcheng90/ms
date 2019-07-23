package com.example.admin;

import com.example.common.resource.annotation.EnableCustomResourceServer;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@MapperScan(value = "com.example.admin.mapper")
@EnableCustomResourceServer
@EnableFeignClients
@SpringCloudApplication
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        log.info("==================AdminApplication================");
    }

}
