package com.xu.seckill.service;

import com.xu.seckill.bean.Goods;
import com.xu.seckill.bean.Order;
import com.xu.seckill.bean.User;
import com.xu.seckill.controller.GoodsController;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.redis.keysPrefix.SeckillKey;
import com.xu.seckill.util.MD5Util;
import com.xu.seckill.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeckillService {
    private static Logger log = LoggerFactory.getLogger(SeckillService.class);
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

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


    // 保证这三个操作，减库存 下订单 写入秒杀订单是一个事物
    @Transactional
    public boolean seckill(User user, Goods goods) {
        boolean reduceSuccess = goodsService.reduceGoodsStock(goods.getId());
        if (reduceSuccess) {
            boolean createSuccess = orderService.createOrder(user, goods);
            return createSuccess && reduceSuccess;
        }
        return false;
    }

}
