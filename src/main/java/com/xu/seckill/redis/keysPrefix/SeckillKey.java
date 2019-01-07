package com.xu.seckill.redis.keysPrefix;

/**
 * Created by jiangyunxiong on 2018/5/29.
 */
public class SeckillKey extends BasePrefix {
    private SeckillKey(String prefix) {
        super(prefix);
    }

    public static SeckillKey isGoodsOver = new SeckillKey("go");
    public static SeckillKey SECKILL_PATH = new SeckillKey("SECKILL_PATH");
}
