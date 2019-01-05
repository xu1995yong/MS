# 秒杀系统

### 思路

1. 卖家选定秒杀商品，设置商品数量，开始结束时间，若商品库存充足，则系统扣除指定数量的商品。


### 接下来要学习
- SpringBoot 自定义方法参数解析器HandlerMethodArgumentResolver

### BUG FIX LOG
- 关于页面中时间显示与数据库中存储不一致，原因：使用com.mysql.cj.jdbc.Driver， 需要指定时区serverTimezone，而mysql数据库使用服务器时区，


WebMvcConfigurerAdapter在spring5中被弃用，该类是WebMvcConfigurer接口的空实现，但是在jdk8中提供了接口的默认方法，所以该类不再被需要。
可以直接实现WebMvcConfigurer接口来进行mvc的配置。