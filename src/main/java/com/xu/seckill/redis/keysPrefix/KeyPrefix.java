package com.xu.seckill.redis.keysPrefix;

public class KeyPrefix {

    private String prefix;


    public KeyPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
