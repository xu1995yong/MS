package com.xu.seckill.redis.keysPrefix;

public interface KeyPrefix {
    //有效期
    int expireSeconds();

    // 前缀
    String getPrefix();
}
