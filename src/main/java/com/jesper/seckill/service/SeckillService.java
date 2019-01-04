package com.jesper.seckill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jesper.seckill.bean.OrderInfo;
import com.jesper.seckill.bean.SeckillOrder;
import com.jesper.seckill.bean.User;
import com.jesper.seckill.redis.KeyPrefix;
import com.jesper.seckill.redis.RedisService;
import com.jesper.seckill.redis.SeckillKey;
import com.jesper.seckill.util.MD5Util;
import com.jesper.seckill.util.UUIDUtil;
import com.jesper.seckill.vo.GoodsVo;

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

//
//	private String getMD5(long seckillGoodsId) {
//		// 结合秒杀的商品id与混淆字符串生成通过md5加密
//		String base = seckillGoodsId + "/" + salt;
//		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
//		return md5;
//	}

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

	public long getSeckillResult(long userId, long goodsId) {
		SeckillOrder order = orderService.getOrderByUserIdGoodsId(userId, goodsId);
		if (order != null) {
			return order.getOrderId();
		} else {
			boolean isOver = getGoodsOver(goodsId);
			if (isOver) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	private void setGoodsOver(Long goodsId) {
		redisService.set(SeckillKey.isGoodsOver, "" + goodsId, true);
	}

	private boolean getGoodsOver(long goodsId) {
		return redisService.exists(SeckillKey.isGoodsOver, "" + goodsId);
	}
}
