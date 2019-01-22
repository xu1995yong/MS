package com.xu.seckill.mq;

import com.xu.seckill.bean.Goods;
import com.xu.seckill.bean.Order;
import com.xu.seckill.exception.GlobalException;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.result.CodeMsg;
import com.xu.seckill.service.GoodsService;
import com.xu.seckill.service.MSService;
import com.xu.seckill.service.OrderService;
import com.xu.seckill.service.UserService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "MS", consumerGroup = "order-paid-consumer")
public class MQReceiver implements RocketMQListener<Order> {
    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MSService seckillService;


    @Override
    public void onMessage(Order order) {
        log.debug("接受到消息:" + order);
        long goodsId = order.getGoodsId();
        Goods goods = goodsService.getGoodsById(goodsId);
        int stock = goods.getStock();
        if (stock <= 0) {
            throw new GlobalException(CodeMsg.STOCK_ERROR);
        }
        // 减库存 下订单 写入秒杀订单
        seckillService.doSeckill(order);
    }


}
