秒杀系统

1. 卖家选定秒杀商品，设置商品数量，开始结束时间，若商品库存充足，则系统扣除指定数量的商品。


BUG FIX LOG
- 关于页面中时间显示与数据库中存储不一致，原因：使用com.mysql.cj.jdbc.Driver， 需要指定时区serverTimezone，而mysql数据库使用服务器时区，



ttt