## spring cloud 学习记录

## 项目使用技术：



## 环境：

    - 安装redis
    - 安装rabbitmq
    - 安装jdk1.8
    - 安装FastFDS
    
## 项目结构
```
ms 
├─logs  日志文件
│
├─core-service 系统服务(微服务管理)
│  ├─core-config 配置中心
│  ├─core-gateway 网关中心
│  ├─core-message-queue 消息列队服务
│  └─core-service-register 服务注册中心
│ 
├─lib-witschool 公共库
│ 
├─ms-common 公共服务
│ 
├─ms-main 系统入口（统一认证中心）
│  
├─ms-witschool （微服务） 

```  
    
## 端口号介绍:

| 服务 | 端口号 | 备注 |  
|:----:|:----:|:----:|  
| ms-core-service-register | 7100 |  ​服务注册中心  |  
| ms-core-config | 7200 |  配置中心  |  
| mms-core-gateway | 7300 |  网关中心  |  
| ms-core-message-queue | 7400 |  消息列队服务  |  
| ms-main | 7500 |  统一认证服务  |  
| ms-common | 7600 |  公共服务  |  

## 启动顺序:
```
1、​服务注册中心
2、配置中心
3、统一认证服务
其他随便
```

## swagger访问地址：

    http://localhost:7300/swagger-ui.html    

​

## 多数据源 和 主从库 参考blog 
    
    Spring Boot + Mybatis多数据源和动态数据源配置
    项目需要同时连接两个不同的数据库A, B，并且它们都为主从架构，一台写库，多台读库
    https://blog.csdn.net/neosmith/article/details/61202084
    
    Mybatisplus整合sharding-jdbc
    https://blog.csdn.net/lijiqidong/article/details/78498428
    
    
    Mybatisplus整合dynamic-datasource-spring-boot-starter
    https://gitee.com/baomidou/dynamic-datasource-spring-boot-starter
    
    
    配置多数据出现的问题集合
    https://www.cnblogs.com/jpfss/p/8295692.html
	
	
	


一起学习可以加下面QQ群，欢迎各位大神
交流QQ群：17470566
本人QQ：913624256
如果喜欢，记得star fork 谢谢您的关注 x_springboot会持续维护	