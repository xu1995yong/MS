package com.xu.seckill.rabbitmq;

import com.xu.seckill.bean.User;


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
