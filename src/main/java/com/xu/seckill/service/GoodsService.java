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

    public List<Goods> listGoodsVo() {
        List<Goods> mSGoodsList = (List<Goods>) redisService.get(GoodsKey.GOODS_LIST, "");
        if (Objects.isNull(mSGoodsList)) {
            mSGoodsList = goodsMapper.getGoodslist();
            redisService.set(GoodsKey.GOODS_LIST, "", mSGoodsList);
        }
        return mSGoodsList;
    }

    public Goods getMSGoodsById(long goodsId) {
        //   Goods mSGoods = (Goods) redisService.get(GoodsKey.GOODS_DETAIL, "" + goodsId);
        Goods goods = goodsMapper.getGoodsById(goodsId);
        if (Objects.isNull(goods)) {

            //redisService.set(GoodsKey.GOODS_DETAIL, "" + msGoodsId, mSGoods);
        }
        return goods;
    }

    public boolean reduceGoodsStock(Long goodsId) {
        int ret = goodsMapper.reduceGoodsStock(goodsId);
        return ret > 0;
    }
}
