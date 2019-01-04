package com.xu.seckill.service;

import java.util.List;

import com.xu.seckill.bean.SeckillGoods;
import com.xu.seckill.mapper.GoodsMapper;
import com.xu.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsService {

	// 乐观锁冲突最大重试次数
	private static final int DEFAULT_MAX_RETRIES = 5;

	@Autowired
    GoodsMapper goodsMapper;

	public List<GoodsVo> listGoodsVo() {
		return goodsMapper.listGoodsVo();
	}

	public GoodsVo getGoodsVoByGoodsId(long goodsId) {
		return goodsMapper.getGoodsVoByGoodsId(goodsId);
	}

	public boolean reduceStock(GoodsVo goods) {
		int numAttempts = 0;
		int ret = 0;
		SeckillGoods sg = new SeckillGoods();
		sg.setGoodsId(goods.getId());
		sg.setVersion(goods.getVersion());
		do {
			numAttempts++;
			try {
				sg.setVersion(goodsMapper.getVersionByGoodsId(goods.getId()));
				ret = goodsMapper.reduceStockByVersion(sg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (ret != 0)
				break;
		} while (numAttempts < DEFAULT_MAX_RETRIES);

		return ret > 0;
	}
}
