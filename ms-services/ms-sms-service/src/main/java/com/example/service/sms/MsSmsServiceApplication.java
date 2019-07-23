package com.example.service.sms;

import com.example.common.resource.annotation.EnableCustomResourceServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;


@Slf4j
@EnableCustomResourceServer
@SpringCloudApplication
public class MsSmsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsSmsServiceApplication.class, args);
        log.info("==================MsSmsServiceApplication启动成功===================");
    }

}
