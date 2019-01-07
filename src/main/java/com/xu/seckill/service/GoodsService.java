package com.xu.seckill.service;

import com.xu.seckill.bean.MSGoods;
import com.xu.seckill.mapper.MSGoodsMapper;
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
    MSGoodsMapper msGoodsMapper;

    public List<MSGoods> listGoodsVo() {
        List<MSGoods> mSGoodsList = (List<MSGoods>) redisService.get(GoodsKey.GOODS_LIST, "");
        if (Objects.isNull(mSGoodsList)) {
            mSGoodsList = msGoodsMapper.getMSGoodslist();
            redisService.set(GoodsKey.GOODS_LIST, "", mSGoodsList);
        }
        return mSGoodsList;
    }

    public MSGoods getMSGoodsById(long msGoodsId) {
        MSGoods mSGoods = (MSGoods) redisService.get(GoodsKey.GOODS_DETAIL, "" + msGoodsId);
        if (Objects.isNull(mSGoods)) {
            mSGoods = msGoodsMapper.getMSGoodsById(msGoodsId);
            redisService.set(GoodsKey.GOODS_DETAIL, "" + msGoodsId, mSGoods);
        }
        return mSGoods;
    }

    public boolean reduceGoodsStock(Long mSGoodsId) {
        int ret = msGoodsMapper.reduceGoodsStock(mSGoodsId);
        return ret > 0;
    }
}
