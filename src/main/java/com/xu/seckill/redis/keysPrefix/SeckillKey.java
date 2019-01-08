package com.xu.seckill.redis.keysPrefix;

public class SeckillKey extends KeyPrefix {
    private SeckillKey(String prefix) {
        super(prefix);
    }

    public static SeckillKey isGoodsOver = new SeckillKey("go");
    public static SeckillKey SECKILL_PATH = new SeckillKey("SECKILL_PATH:");
}
