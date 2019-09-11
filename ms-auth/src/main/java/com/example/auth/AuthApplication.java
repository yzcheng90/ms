package com.example.auth;

import com.example.common.feign.annotation.EnableCustomFeignClients;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@Slf4j
@MapperScan(value = {"com.example.auth.mapper"})
@EnableCustomFeignClients
@SpringCloudApplication
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        log.info("===============AuthApplication===============");
    }

}
