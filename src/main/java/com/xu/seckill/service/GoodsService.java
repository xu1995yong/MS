package com.xu.seckill.service;

import java.util.List;

import com.xu.seckill.bean.SeckillGoods;
import com.xu.seckill.mapper.GoodsMapper;
import com.xu.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    public List<GoodsVo> listGoodsVo() {
        return goodsMapper.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsMapper.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceGoodsStock(GoodsVo goods) {
        SeckillGoods sg = new SeckillGoods();
        sg.setGoodsId(goods.getId());
        sg.setVersion(goods.getVersion());
        int ret = goodsMapper.reduceGoodsStock(sg);

        return ret > 0;
    }
}
