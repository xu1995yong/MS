package com.xu.seckill.redis.keysPrefix;


public class OrderKey extends KeyPrefix {

    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey SeckillOrderByUid = new OrderKey("seckill");
    public static OrderKey ORDER_DETAIL = new OrderKey("ORDER_DETAIL:");
}
