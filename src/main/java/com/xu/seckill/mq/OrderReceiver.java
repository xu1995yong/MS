package com.xu.seckill.mq;

import com.xu.seckill.bean.Goods;
import com.xu.seckill.bean.Order;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.redis.keysPrefix.OrderKey;
import com.xu.seckill.result.CodeMsg;
import com.xu.seckill.service.GoodsService;
import com.xu.seckill.service.MSService;
import com.xu.seckill.service.OrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "MS", consumerGroup = "order-paid-consumer")
public class OrderReceiver implements RocketMQListener<Order> {
    private static Logger log = LoggerFactory.getLogger(OrderReceiver.class);

    @Autowired
    MSService seckillService;


    @Override
    public void onMessage(Order order) {
        log.debug("接受到消息:" + order);

        seckillService.doSeckill(order);
    }


}
