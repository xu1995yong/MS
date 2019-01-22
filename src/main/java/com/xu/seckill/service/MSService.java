package com.xu.seckill.service;

import com.xu.seckill.bean.Goods;
import com.xu.seckill.bean.Order;
import com.xu.seckill.mq.MQSender;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.redis.keysPrefix.GoodsKey;
import com.xu.seckill.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class MSService {
    private static Logger log = LoggerFactory.getLogger(MSService.class);
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;
    @Autowired
    MQSender sender;


    public String seckill(long userId, long goodsId, int goodsCount) {

        //将商品库存存入redis
        boolean exist = redisService.exists(GoodsKey.GOODS_STOCK, goodsId);
        if (!exist) {
            Goods goods = goodsService.getGoodsById(goodsId);
            redisService.set(GoodsKey.GOODS_STOCK, goods.getId(), goods.getStock());
        }


        boolean success = redisService.decrStock(GoodsKey.GOODS_STOCK, goodsId, goodsCount);
        if (success) {
            String orderId = UUIDUtil.uuid();
            int status = 0;
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Order order = new Order(orderId, userId, goodsId, goodsCount, status, now);
            log.debug("OrderId is {}", order);
            sender.asyncSendMessage(order);
            sender.sendDelayMessage(order);
            return orderId;
        } else {
            return null;
        }
    }


    @Transactional
    public boolean doSeckill(Order order) {
        boolean reduceSuccess = goodsService.reduceGoodsStock(order.getGoodsId());
        if (reduceSuccess) {
            boolean createSuccess = orderService.createOrder(order);
            return createSuccess;
        }
        return false;
    }

}
