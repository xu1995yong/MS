package com.xu.seckill.redis.keysPrefix;

public class GoodsKey extends KeyPrefix {

    private GoodsKey(String prefix) {
        super(prefix);
    }

    //   public static GoodsKey getGoodsList = new GoodsKey(60, "gl");
    public static GoodsKey GOODS_DETAIL = new GoodsKey("GOODS_DETAIL:");
    public static GoodsKey GOODS_LIST = new GoodsKey("GOODS_LIST:");
    public static GoodsKey GOODS_STOCK = new GoodsKey("GOODS_STOCK:");
}
