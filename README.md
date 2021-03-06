# 秒杀系统
一个典型的秒杀系统的设计与实现

### 秒杀架构设计理念
1. 限流： 鉴于只有少部分用户能够秒杀成功，所以要限制大部分流量，只允许少部分流量进入服务后端。
2. 削峰：对于秒杀系统瞬时会有大量用户涌入，所以在抢购一开始会有很高的瞬间峰值。高峰值流量是压垮系统很重要的原因，所以如何把瞬间的高流量变成一段时间平稳的流量也是设计秒杀系统很重要的思路。实现削峰的常用的方法有利用缓存和消息中间件等技术。
3. 异步处理：秒杀系统是一个高并发系统，采用异步处理模式可以极大地提高系统并发量，其实异步处理就是削峰的一种实现方式。
4. 内存缓存：秒杀系统最大的瓶颈一般都是数据库读写，由于数据库读写属于磁盘IO，性能很低，如果能够把部分数据或业务逻辑转移到内存缓存，效率会有极大地提升。
5. 可拓展：当然如果我们想支持更多用户，更大的并发，最好就将系统设计成弹性可拓展的，如果流量来了，拓展机器就好了。像淘宝、京东等双十一活动时会增加大量机器应对交易高峰。
 
 
### 秒杀系统业务设计

1. 卖家选定秒杀商品，设置商品数量，开始结束时间，若商品库存充足，则系统扣除指定数量的商品。

2. 秒杀之后的业务逻辑:点击秒杀按钮，若Redis减库存成功，则转到订单页面创建订单，并使用消息队列将秒杀消息发送到mysql数据库。否则返回秒杀失败页面。


### 秒杀系统业务逻辑分析 
假设我们的秒杀场景：某商品10件物品待秒. 假设有100台web服务器(假设web服务器是Nginx + Tomcat),n台app服务器,n个数据库 

1. 秒杀入口隐藏：为了防止用户直接调用秒杀地址接口来机器刷单。当秒杀真正开始时，服务器端才创建一个uuid，该uuid作为商品的秒杀url的一部分，然后服务器端将该uuid发送给用户，并将该uuid以商品id为键保存在redis中做验证。
2. Controllor层过滤：每台web服务器的业务处理模块里做个计数器AtomicInteger(10)=待秒商品总数, decreaseAndGet()>＝0的继续做后续处理, <0的直接返回秒杀结束页面. 
   这样经过第一步的处理只剩下100台*10个=1000个请求。
3. Redis层过滤：redis中保存着以商品id作为key以库存为value，每个web服务器在接到每个请求的同时, 向redis服务器发起请求, 利用redis的乐观锁，decr(key,1)操作返回值>=0的继续处理, 其余的返回秒杀失败页面. 这样处理后只剩下100台中最快速到达的10个请求.
数据库
2. 使用redis防止同一用户同时对多个商品进行秒杀。以用户Id为key
3. 使用rocketmq的延迟队列，实现未付款订单的及时关闭

### 秒杀系统业务实现原理
1. 秒杀入口隐藏：

2. Redis乐观锁

3. 单点登录：同域下的单点登录

 

### 接下来要学习的知识点
- SpringBoot 自定义方法参数解析器HandlerMethodArgumentResolver
- SpringBoot 拦截器做登录验证
- WebMvcConfigurer接口
- Redis实现乐观锁的原理
 

 


SpringBoot 测试模块 自动注入失败，要在@SpringBootTest注解中加入启动类名(classes = MsApplication.class)

docker命令 启动一个已经停止的容器 docker start 重启容器 docker restart 停止容器 docker stop

### 问题记录
为什么redis是单线程的，