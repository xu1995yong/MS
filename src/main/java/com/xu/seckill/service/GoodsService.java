package com.xu.seckill.service;

import com.xu.seckill.bean.Goods;
import com.xu.seckill.mapper.GoodsMapper;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.redis.keysPrefix.GoodsKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class GoodsService {
    @Autowired
    RedisService redisService;

    @Autowired
    GoodsMapper goodsMapper;

    public List<Goods> getGoodsList() {
        List<Goods> goodsList = (List<Goods>) redisService.get(GoodsKey.GOODS_LIST, "ALL");
        if (Objects.isNull(goodsList)) {
            goodsList = goodsMapper.getGoodslist();
            redisService.set(GoodsKey.GOODS_LIST, "", goodsList);
        }
        return goodsList;
    }

    public Goods getGoodsById(long goodsId) {
        Goods goods = (Goods) redisService.get(GoodsKey.GOODS_DETAIL, goodsId);
        if (Objects.isNull(goods)) {
            goods = goodsMapper.getGoodsById(goodsId);
            redisService.set(GoodsKey.GOODS_DETAIL, goodsId, goods);
        }
        return goods;
    }

    public boolean reduceGoodsStock(Long goodsId) {
        int ret = goodsMapper.reduceGoodsStock(goodsId);
        return ret > 0;
    }
}
