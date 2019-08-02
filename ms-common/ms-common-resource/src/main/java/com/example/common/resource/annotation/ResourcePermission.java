package com.example.common.resource.annotation;

import java.lang.annotation.*;

/**
 * @author czx
 * @title: ResourcePermission
 * @projectName ms
 * @description: TODO 资源权限
 * @date 2019/8/29:25
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResourcePermission {

    String value() default "";

}
