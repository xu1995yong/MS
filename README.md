# 秒杀系统
一个典型的秒杀系统的设计与实现

### 业务设计

1. 卖家选定秒杀商品，设置商品数量，开始结束时间，若商品库存充足，则系统扣除指定数量的商品。

2. 秒杀之后的业务逻辑:点击秒杀按钮，若减库存成功/创建订单成功，则转到订单页面，否则返回秒杀失败页面。

### 接下来要学习
- SpringBoot 自定义方法参数解析器HandlerMethodArgumentResolver
- SpringBoot 拦截器做登录验证

### BUG FIX LOG
- 关于页面中时间显示与数据库中存储不一致，原因：使用com.mysql.cj.jdbc.Driver， 需要指定时区serverTimezone，而mysql数据库使用服务器时区，


WebMvcConfigurerAdapter在spring5中被弃用，该类是WebMvcConfigurer接口的空实现，但是在jdk8中提供了接口的默认方法，所以该类不再被需要。
可以直接实现WebMvcConfigurer接口来进行mvc的配置。