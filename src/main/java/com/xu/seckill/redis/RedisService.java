package com.xu.seckill.redis;

import com.xu.seckill.redis.keysPrefix.KeyPrefix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    private static Logger log = LoggerFactory.getLogger(RedisService.class);
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    public Object get(KeyPrefix prefix, Object key) {
        String realKey = prefix.getPrefix() + key;
        return redisTemplate.opsForValue().get(realKey);
    }

    public int getStock(KeyPrefix prefix, Object key) {
        String realKey = prefix.getPrefix() + key;
        if (!exists(prefix, key)) {
            throw new RuntimeException("key不存在:" + realKey);
        }
        return Integer.valueOf(stringRedisTemplate.opsForValue().get(realKey));
    }

//    public Object getAll(KeyPrefix prefix, String key) {
//        String realKey = prefix.getPrefix() + key;
//        return redisTemplate.opsForList().
//    }


    public <T> void set(KeyPrefix prefix, Object key, Object value) {
        String realKey = prefix.getPrefix() + key;
        redisTemplate.opsForValue().set(realKey, value);
    }

    public boolean delete(KeyPrefix prefix, Object key) {
        String realKey = prefix.getPrefix() + key;
        return stringRedisTemplate.delete(realKey);
    }

    public <T> boolean exists(KeyPrefix prefix, Object key) {
        String realKey = prefix.getPrefix() + key;
        return stringRedisTemplate.hasKey(realKey);
    }


    //redis预减库存
    public boolean decrStock(KeyPrefix prefix, Object key, int goodsCount) {
        //redis中是否存在该key
        String realKey = prefix.getPrefix() + key;

        return stringRedisTemplate.opsForValue().decrement(realKey) >= 0;
    }
}


