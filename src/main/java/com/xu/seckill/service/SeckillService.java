package com.xu.seckill.service;

import com.xu.seckill.bean.MSGoods;
import com.xu.seckill.bean.User;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.redis.keysPrefix.SeckillKey;
import com.xu.seckill.util.MD5Util;
import com.xu.seckill.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    private final String salt = "12sadasadsafafsafs。/。，";

    public String createPath(User user, long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + salt);
        redisService.set(SeckillKey.SECKILL_PATH, "" + user.getId() + ":" + goodsId, str);
        return str;
    }

    public boolean validPath(String path, User user, long goodsId) {
        String str = (String) redisService.get(SeckillKey.SECKILL_PATH, "" + user.getId() + ":" + goodsId);
        return path.equals(str);
    }


    // 保证这三个操作，减库存 下订单 写入秒杀订单是一个事物
    @Transactional
    public boolean seckill(User user, MSGoods msGoods) {
        // 减库存
        boolean success = goodsService.reduceGoodsStock(msGoods.getGoodsId());
        if (success) {
            // 下订单 写入秒杀订单
//            MSOrder order = orderService.createOrder(user, msGoods);
//            if (order != null) {
//                return true;
//            }
        }
        return false;
    }

}
