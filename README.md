# 秒杀系统
一个典型的秒杀系统的设计与实现

### 秒杀架构设计理念
1. 限流： 鉴于只有少部分用户能够秒杀成功，所以要限制大部分流量，只允许少部分流量进入服务后端。
2. 削峰：对于秒杀系统瞬时会有大量用户涌入，所以在抢购一开始会有很高的瞬间峰值。高峰值流量是压垮系统很重要的原因，所以如何把瞬间的高流量变成一段时间平稳的流量也是设计秒杀系统很重要的思路。实现削峰的常用的方法有利用缓存和消息中间件等技术。
3. 异步处理：秒杀系统是一个高并发系统，采用异步处理模式可以极大地提高系统并发量，其实异步处理就是削峰的一种实现方式。
4. 内存缓存：秒杀系统最大的瓶颈一般都是数据库读写，由于数据库读写属于磁盘IO，性能很低，如果能够把部分数据或业务逻辑转移到内存缓存，效率会有极大地提升。
5. 可拓展：当然如果我们想支持更多用户，更大的并发，最好就将系统设计成弹性可拓展的，如果流量来了，拓展机器就好了。像淘宝、京东等双十一活动时会增加大量机器应对交易高峰。
 
 
### 项目亮点 

### 业务设计

1. 卖家选定秒杀商品，设置商品数量，开始结束时间，若商品库存充足，则系统扣除指定数量的商品。

2. 秒杀之后的业务逻辑:点击秒杀按钮，若Redis减库存成功，则转到订单页面创建订单，并使用消息队列将秒杀消息发送到mysql数据库。否则返回秒杀失败页面。

### TODO
- Redis乐观锁 防止超卖

### 接下来要学习
- SpringBoot 自定义方法参数解析器HandlerMethodArgumentResolver
- SpringBoot 拦截器做登录验证
- Redis实现乐观锁的原理
### BUG FIX LOG
- 关于页面中时间显示与数据库中存储不一致，原因：使用com.mysql.cj.jdbc.Driver， 需要指定时区serverTimezone，而mysql数据库使用服务器时区，
- Ajax请求不能重定向
- Rdis有效期 与 数据反序列化

WebMvcConfigurerAdapter在spring5中被弃用，该类是WebMvcConfigurer接口的空实现，但是在jdk8中提供了接口的默认方法，所以该类不再被需要。
可以直接实现WebMvcConfigurer接口来进行mvc的配置。


SpringBoot 测试模块 自动注入失败，要在@SpringBootTest注解中加入启动类名(classes = MsApplication.class)

docker命令 启动一个已经停止的容器 docker start 重启容器 docker restart 停止容器 docker stop