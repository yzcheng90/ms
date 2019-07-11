package com.example.common.resource.security;

import com.example.common.core.constants.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Description //TODO 动态注入bean
 * 需要 在yml中开启 main.allow-bean-definition-overriding: true
 * @Date 16:57 2019/4/3
 **/
@Slf4j
public class CustomResourceBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * @Author czx
     * @Description //TODO  根据注解值动态注入资源服务器的相关属性
     * @Date 16:59 2019/4/3
     * @Param [annotationMetadata, beanDefinitionRegistry]
     * @return void
     **/
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        if (beanDefinitionRegistry.isBeanNameInUse(SecurityConstants.RESOURCE_SERVER_CONFIGURER)) {
            log.warn("本地存在资源服务器配置，覆盖默认配置:" + SecurityConstants.RESOURCE_SERVER_CONFIGURER);
            return;
        }

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ResourceServerConfig.class);
        beanDefinitionRegistry.registerBeanDefinition(SecurityConstants.RESOURCE_SERVER_CONFIGURER, beanDefinition);

    }
}
