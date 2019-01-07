package com.xu.seckill.redis.keysPrefix;

public class GoodsKey extends BasePrefix {

    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    //   public static GoodsKey getGoodsList = new GoodsKey(60, "gl");
    public static GoodsKey GOODS_DETAIL = new GoodsKey(60, "GOODS_DETAIL:");
    public static GoodsKey GOODS_LIST = new GoodsKey(60, "GOODS_LIST:");
    public static GoodsKey GOODS_STOCK = new GoodsKey(0, "GOODS_STOCK:");
}
