package com.example.common.resource.annotation;

import java.lang.annotation.*;


/**
 * @author czx
 * @title: AuthIgnore
 * @projectName ms
 * @description: TODO 忽略token 直接开放api
 * @date 2019/7/24 19:21
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthIgnore {
}
