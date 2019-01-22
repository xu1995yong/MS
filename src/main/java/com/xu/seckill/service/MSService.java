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

    //标注商品已经秒杀完
    ConcurrentHashMap<Long, Boolean> isOver = new ConcurrentHashMap();

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

    //如果秒杀失败，返回空字符串，库存不足返回null，秒杀成功返回orderId
    public String seckill(long userId, long goodsId, int goodsCount) {
        if (isOver.containsKey(goodsId) && isOver.get(goodsId)) {
            return null;
        }

        //将商品库存存入redis
        boolean exist = redisService.exists(GoodsKey.GOODS_STOCK, goodsId);
        if (!exist) {
            Goods goods = goodsService.getGoodsById(goodsId);
            redisService.set(GoodsKey.GOODS_STOCK, goods.getId(), goods.getStock());
        }

//         判断重复秒杀
//        SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
//        if (order != null) {
//            return Result.error(CodeMsg.REPEATE_SECKILL);
//        }

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


    // 保证这三个操作是一个事物
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
