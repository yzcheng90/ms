package com.example.common.feign.annotation;

import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

/**
 * @author czx
 * @title: EnableCustomFeignClients
 * @projectName ms
 * @description: TODO
 * @date 2019/8/710:38
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
public @interface EnableCustomFeignClients {
    String[] basePackages() default {};
}
