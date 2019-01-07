package com.xu.seckill.service;

import com.xu.seckill.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private static Logger log = LoggerFactory.getLogger(OrderService.class);
//    @Autowired
    //  MSOrderMapper orderMapper;

    @Autowired
    RedisService redisService;

//    public SeckillOrder getOrderByUserIdGoodsId(long userId, long goodsId) {
//        return (SeckillOrder) redisService.get(OrderKey.getSeckillOrderByUidGid, "" + userId + "_" + goodsId);
//    }
//
//    public OrderInfo getOrderById(long orderId) {
//        return orderMapper.getOrderById(orderId);
//    }

    /**
     * 因为要同时分别在订单详情表和秒杀订单表都新增一条数据，所以要保证两个操作是一个事物
     */
//    @Transactional
//    public OrderInfo createOrder(User user, Goods msGoods) {
////        OrderInfo orderInfo = new OrderInfo();
////        orderInfo.setCreateDate(new Date());
////        orderInfo.setDeliveryAddrId(0L);
////        orderInfo.setGoodsCount(1);
////        orderInfo.setGoodsId(msGoods.getGoodsId());
////        orderInfo.setGoodsName(msGoods.getGoodsName());
////        orderInfo.setGoodsPrice(msGoods.getSeckillPrice());
////        orderInfo.setOrderChannel(1);
////        orderInfo.setStatus(0);
////        orderInfo.setUserId(user.getId());
////        orderMapper.insert(orderInfo);
////
////        SeckillOrder seckillOrder = new SeckillOrder();
////        seckillOrder.setGoodsId(msGoods.getGoodsId());
////        seckillOrder.setOrderId(orderInfo.getId());
////        seckillOrder.setUserId(user.getId());
////        log.info("create order" + seckillOrder);
////        // orderMapper.insertSeckillOrder(seckillOrder);
////
////        redisService.set(OrderKey.getSeckillOrderByUidGid, "" + user.getId() + "_" + msGoods.getGoodsId(), seckillOrder);
////
////        return orderInfo;
//    }

}
