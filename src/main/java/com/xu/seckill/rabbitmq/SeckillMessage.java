package com.xu.seckill.rabbitmq;

import com.xu.seckill.bean.User;

/**
 * Created by jiangyunxiong on 2018/5/29.
 *
 * 消息体
 */
public class SeckillMessage {

	@Override
	public String toString() {
		return "SeckillMessage [user=" + user + ", goodsId=" + goodsId + "]";
	}

	private User user;
	private long goodsId;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}
}
