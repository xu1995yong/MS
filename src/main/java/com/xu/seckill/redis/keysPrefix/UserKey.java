package com.xu.seckill.redis.keysPrefix;

public class UserKey extends BasePrefix {
    //TODO
    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;//默认两天


    private UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }


    public static UserKey TOKEN = new UserKey(TOKEN_EXPIRE, "token:"); //
    public static UserKey ID = new UserKey(0, "id:");//

}
