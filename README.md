# MS 是一个spring cloud 开发框架
### （立志成为最简洁，最好用的框架..）

### 相关技术实现博客请看《[MS系列](https://blog.csdn.net/qq_15273441/article/category/9183057)》

#### 项目特点

- 深度定制 spring security oauth2 除了原有4种模式，还扩展支持 手机号、QQ、微信等等第三方获取token
- 深度定制资源服务只需要一个注解即可被 oauth2 管理
- 基于用户的网关限流维度，可控制到每个用户
- 多个服务之间调用自动维护token无感传递
- 服务安全访问限制，只能从网关访问，不能直接访问服务 
- 对于不需要鉴权的接口，只需要加上一个注解就可以访问了
- 持续更新中...	

#### 使用技术

|  技术   |   版本   |
| ---- | ---- |
|   spring-boot   |   2.1.3.RELEASE   |
|   spring-cloud   |   Greenwich.RELEASE   |
|   spring-security-oauth2   |   2.3.5.RELEASE   |
|   mybatis-plus   |   3.1.0   |


#### 依赖环境

- jdk1.8
- redis 3.2+
- lombok 插件
- mysql 5.7+

####  启动顺序

    1、MS-Eureka
    2、MS-Config
    3、MS-Auth
    4、MS-Gateway
    5、MS-Admin
    6、MS-SMS
    
    PS：1、2 启动后其他随便
    

#### 系统架构图
![image](https://github.com/yzcheng90/MS/blob/master/doc/architecture_pic.png)

#### 项目目录
```
MS
├─doc  项目SQL语句
│
├─ms-auth 统一认证服务
├─ms-common 系统公共模块
│   ├─ms-common-cache 公共缓存模块
│   ├─ms-common-core 公共组件模块
│   ├─ms-common-feign 公共Feign模块
│   ├─ms-common-gateway 网关限流模块
│   ├─ms-common-interceptor 公共拦截器模块
│   ├─ms-common-resource 公共资源服务模块
│   ├─ms-common-user 公共用户信息
├─ms-config 配置中心
├─ms-eureka 服务注册中心
├─ms-gateway 网关服务
├─ms-services 微服务
│   ├─ms-admin-service admin服务
│   ├─ms-sms-service 短信服务

```

一起学习可以加下面QQ群，欢迎各位大神
交流QQ群：17470566
本人QQ：913624256
如果喜欢，记得star fork 谢谢您的关注 这个会持续维护	