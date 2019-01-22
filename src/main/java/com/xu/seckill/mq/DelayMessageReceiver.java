package com.xu.seckill.mq;

import com.xu.seckill.bean.Order;
import com.xu.seckill.service.OrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "OverTimeOrder", consumerGroup = "Over-Time-Order")
public class DelayMessageReceiver implements RocketMQListener<Order> {
    private static Logger log = LoggerFactory.getLogger(DelayMessageReceiver.class);
    @Autowired
    OrderService orderService;

    @Override
    public void onMessage(Order order) {
        log.debug("接收到延迟消息：{}", order);

        order.setStatus(-1);
        int val = orderService.updateOrder(order);
        if (val == 1) {
            log.debug("关闭订单成功：{}", order);
        } else {
            log.debug("关闭订单失败：{}", order);
        }
    }
}
