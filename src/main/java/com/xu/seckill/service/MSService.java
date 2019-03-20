package com.xu.seckill.service;

import com.xu.seckill.bean.Goods;
import com.xu.seckill.bean.Order;
import com.xu.seckill.mq.MQSender;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.redis.keysPrefix.GoodsKey;
import com.xu.seckill.redis.keysPrefix.SeckillKey;
import com.xu.seckill.util.MD5Util;
import com.xu.seckill.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.InvalidPropertiesFormatException;
import java.util.concurrent.ConcurrentHashMap;

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


    private final String salt = "12sadasadsafafsafs。/。，";

    public String createPath(long goodsId) {
        String path = (String) redisService.get(SeckillKey.SECKILL_PATH, goodsId);
        if (path != null) {
            return path;
        }
        String str = MD5Util.md5(UUIDUtil.uuid() + salt);
        log.debug("{}号商品路径为：{}", goodsId, str);
        redisService.set(SeckillKey.SECKILL_PATH, goodsId, str);
        return str;
    }

    public boolean validPath(String path, long goodsId) {
        String str = (String) redisService.get(SeckillKey.SECKILL_PATH, goodsId);
        return path.equals(str);
    }


    public String seckill(long userId, long goodsId, int goodsCount) {


        //将商品库存存入redis
        boolean exist = redisService.exists(GoodsKey.GOODS_STOCK, goodsId);
        if (!exist) {
            Goods goods = goodsService.getGoodsById(goodsId);
            redisService.set(GoodsKey.GOODS_STOCK, goods.getId(), goods.getStock());
        }
        //首先判断该商品秒杀是否结束，即商品库存是否剩余
        int stock = redisService.getStock(GoodsKey.GOODS_STOCK, goodsId);
        if (stock < 0) {
            throw new RuntimeException("Redis中库存小于零，秒杀结束");
        }

        boolean success = redisService.decrStock(GoodsKey.GOODS_STOCK, goodsId, goodsCount);
        if (!success) {
            throw new RuntimeException("秒杀失败");
        }
        String orderId = UUIDUtil.uuid();
        int status = 0;
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Order order = new Order(orderId, userId, goodsId, goodsCount, status, now);
        log.debug("Order  is {}", order);
        sender.asyncSendMessage(order);
        sender.sendDelayMessage(order);
        return orderId;
    }


    @Transactional
    public boolean doSeckill(Order order) {
        try {
            goodsService.reduceGoodsStock(order.getGoodsId());
            orderService.createOrder(order);
        } catch (Exception e) {
            redisService.delete(GoodsKey.GOODS_STOCK, order.getGoodsId());
            System.out.println(e);
            throw e;
        }
        return true;
    }

}
