package com.xu.seckill.rabbitmq;

public class SeckillMessage {
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
