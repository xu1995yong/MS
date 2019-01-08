package com.xu.seckill.redis.keysPrefix;

public class UserKey extends KeyPrefix {

    private UserKey(String prefix) {
        super(prefix);
    }

    public static final int EXPIRE_TIME = 60 * 60 * 24;

    public static UserKey USER_TOKEN = new UserKey("USER_TOKEN:");
    public static UserKey USER_ID = new UserKey("USER_ID:");

}
