package com.xu.seckill.rabbitmq;

import java.io.Serializable;

public class SeckillMessage implements Serializable {
    private long userId;
    private long goodsId;

    public SeckillMessage(long userId, long goodsId) {
        this.userId = userId;
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        return "SeckillMessage{" +
                "userId=" + userId +
                ", goodsId=" + goodsId +
                '}';
    }

    public long getUserId() {
        return userId;
    }

    public long getGoodsId() {
        return goodsId;
    }
}
