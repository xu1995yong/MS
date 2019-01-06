package com.xu.seckill.service;

import com.xu.seckill.bean.OrderInfo;
import com.xu.seckill.bean.User;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.redis.SeckillKey;
import com.xu.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xu.seckill.bean.SeckillOrder;
import com.xu.seckill.util.MD5Util;
import com.xu.seckill.util.UUIDUtil;

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
        redisService.set(SeckillKey.isSeckillPath, "" + user.getId() + "_" + goodsId, str);
        return str;
    }

    public boolean validPath(String path, User user, long goodsId) {
        String str = redisService.get(SeckillKey.isSeckillPath, "" + user.getId() + "_" + goodsId, String.class);
        return path.equals(str);
    }


    // 保证这三个操作，减库存 下订单 写入秒杀订单是一个事物
    @Transactional
    public OrderInfo seckill(User user, GoodsVo goods) {
        // 减库存
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            // 下订单 写入秒杀订单
            return orderService.createOrder(user, goods);
        } else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

//    public SeckillOrder getSeckillResult(long userId, long goodsId) {


//        if (order != null) {
//            return order.getOrderId();
//        } else {
//            boolean isOver = getGoodsOver(goodsId);
//            if (isOver) {
//                return -1;
//            } else {
//                return 0;
//            }
//        }
//    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver, "" + goodsId);
    }
}
