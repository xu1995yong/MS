package com.xu.seckill.service;

import com.xu.seckill.bean.Order;
import com.xu.seckill.mapper.OrderMapper;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.redis.keysPrefix.OrderKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private static Logger log = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    OrderMapper orderMapper;

    @Autowired
    RedisService redisService;

//    public Order getOrderByUserId(long userId, long goodsId) {
//        return (Order) redisService.get(OrderKey.getSeckillOrderByUid, "" + userId + "_" + goodsId);
//    }

    public Order getOrderById(String orderId) {
        Order order = (Order) redisService.get(OrderKey.ORDER_DETAIL, orderId);
        if (order == null) {
            order = orderMapper.getById(orderId);

        }
        return order;
    }


    public void createOrder(Order order) {
        log.debug("创建订单：{}", order);
        if (orderMapper.insert(order) != 1) {
            throw new RuntimeException("订单创建失败");
        }


        redisService.set(OrderKey.ORDER_DETAIL, order.getId(), order);
        log.debug("向redis中插入订单" + order);


    }

    public void payOrder() {

    }

    public int updateOrder(Order newOrder) {
        return orderMapper.update(newOrder);
    }

}
