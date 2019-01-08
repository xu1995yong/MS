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

    public Order getOrderById(long orderId) {
        Order order = (Order) redisService.get(OrderKey.ORDER_DETAIL, orderId);
        if (order == null) {
            order = orderMapper.getById(orderId);
        }
        return order;
    }


    public boolean createOrder(User user, Goods goods) {
        int status = 1;
        Timestamp now = new Timestamp(System.currentTimeMillis());

        Order order = new Order(user.getId(), goods.getId(), 1, status, now);
        long orderId = orderMapper.insert(order);
        order.setId(orderId);
        if (orderId > -1) {
            redisService.set(OrderKey.ORDER_DETAIL, orderId, order);
        }
        log.debug("创建订单：{}", order);
        return true;
    }

}
