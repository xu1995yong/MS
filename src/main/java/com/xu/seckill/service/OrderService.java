package com.xu.seckill.service;

import com.xu.seckill.bean.Goods;
import com.xu.seckill.bean.Order;
import com.xu.seckill.bean.User;
import com.xu.seckill.mapper.OrderMapper;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.redis.keysPrefix.OrderKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

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
            redisService.set(OrderKey.ORDER_DETAIL, orderId, order);
        }
        return order;
    }


    public boolean createOrder(Order order) {
        int val = orderMapper.insert(order);
        if (val == 1) {
            redisService.set(OrderKey.ORDER_DETAIL, order.getId(), order);
        }
        log.debug("创建订单：{}", order);
        return true;
    }

}
