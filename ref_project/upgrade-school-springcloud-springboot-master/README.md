# 你好，Springcloud，初次见面，请多多指教

school-springcloud-springboot是一个基于Springcloud的课程管理系统，它是什么系统其实不是很重要，关键是它利用Springboot的开发便利性巧妙地简化了分布式系统基础设施的开发，加入了Springcloud系列框架特有服务发现注册、配置中心、消息总线、负载均衡、断路器、数据监控等功能，可以让你真正的感受到**微服务**的魅力。所以Springcloud几乎是现在微服务开源界最好的产品了。与Dubbo比较起来，Springcloud的使用会让你更加的爱不释手。

## 一、运行工具、技术与环境

* 运行环境：JDK 8，gradle 6.0+
* 技术栈：SpringBoot 2.0+、Druid、Thymeleaf、Mybatis
* **微服务Springcloud技术栈：Spring Cloud Config、Spring Cloud Bus、Eureka、Hystrix、Zuul、Spring Cloud Gateway、Spring Cloud Sleuth、Ribbon、Feign、zipkin、hystrix-dashboard**
* 工具：IntelliJ IDEA、谷歌浏览器、Mysql、RabbitMq

## 二、Springboot快速集成Springcloud关键的依赖
```gradle
dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.2.2.RELEASE")
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR1"
    }
}

```
## 三、各个module介绍
1、eureka-server：基于Eureka的注册中心，提供服务发现与注册。

2、zuul-server：基于zuul微服务网关服务器，提供动态路由与负载分配等等。

3、gateway-server：基于spring自家的Spring Cloud Gateway微服务网关，它是底层是webflux，也可以提供动态路由与负载分配等等。
4、config-server：配置中心服务端。

5、hystrix-dashboard：基于Hystrix的仪表盘组件的module，可以通过网页实时浏览Hystrix的各项指标信息。

6、main-operation：提供CourseType的服务的项目，里面包括配置中心、feign等技术。

7、user-operation：提供User的服务的项目，包括Spring Cloud Sleuth、feign等技术。

8、zipkin-server：Zipkin分布式跟踪系统。

9、main-school：主操作系统。

## 四、使用步骤
1.将项目导入IntelliJ IDEA，gradle加载jar包。

2.将doc文件夹里面的sql脚本导入到mysql中，使得数据库里面有数据。

3、修改main-operation和user-operation数据库配置。

4、向window的hosts添加

   127.0.0.1       eureka8181.com
   
   127.0.0.1       eureka8182.com
   
   127.0.0.1       eureka8183.com
   
5、开启rabbitMQ和mysql

6.项目启动顺序（这样启动比较合适，当然熟悉之后自行更改启动顺序，**还有，还有：计算机内存至少12g**）：

（1）zipkin-server

（2）eureka-server1、eureka-server2、eureka-server3

（3）gateway-server

（4）config-server

（5）user-operation

（6）school-operation1、school-operation2、school-operation3

（7）main-operation

（8）hystrix-dashboard


4.打开浏览器，输入网址[http://localhost:8095](http://localhost:8095)即可浏览（账号：000101  密码：123456）。

**注意：** 若是该项目对初学者有困难的话，请先学习school-springcloud-springboot的简化版项目，地址如下：
* 码云：[https://gitee.com/smirk/easy-springcloud](https://gitee.com/smirk/easy-springcloud)
* github：[https://github.com/xiaoze-smirk/school-springcloud-springboot](https://github.com/xiaoze-smirk/school-springcloud-springboot)

## 五、总结
您要是喜欢，请拿去。您的受益，是我最大的动力。

------

smirk小泽   
2018 年 07月17日    
