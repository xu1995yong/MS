package com.xu.seckill.service;

import com.xu.seckill.bean.OrderInfo;
import com.xu.seckill.bean.SeckillOrder;
import com.xu.seckill.bean.User;
import com.xu.seckill.mapper.OrderMapper;
import com.xu.seckill.redis.OrderKey;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.vo.GoodsVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {
	private static Logger log = LoggerFactory.getLogger(OrderService.class);
	@Autowired
    OrderMapper orderMapper;

	@Autowired
    RedisService redisService;

	public SeckillOrder getOrderByUserIdGoodsId(long userId, long goodsId) {
		return redisService.get(OrderKey.getSeckillOrderByUidGid, "" + userId + "_" + goodsId, SeckillOrder.class);
	}

	public OrderInfo getOrderById(long orderId) {
		return orderMapper.getOrderById(orderId);
	}

	/**
	 * 因为要同时分别在订单详情表和秒杀订单表都新增一条数据，所以要保证两个操作是一个事物
	 */
	@Transactional
	public OrderInfo createOrder(User user, GoodsVo goods) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setGoodsName(goods.getGoodsName());
		orderInfo.setGoodsPrice(goods.getGoodsPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(user.getId());
		orderMapper.insert(orderInfo);

		SeckillOrder seckillOrder = new SeckillOrder();
		seckillOrder.setGoodsId(goods.getId());
		seckillOrder.setOrderId(orderInfo.getId());
		seckillOrder.setUserId(user.getId());
		log.info("create order" + seckillOrder);
		// orderMapper.insertSeckillOrder(seckillOrder);

		redisService.set(OrderKey.getSeckillOrderByUidGid, "" + user.getId() + "_" + goods.getId(), seckillOrder);

		return orderInfo;
	}

}