package com.xu.seckill.redis;

import com.xu.seckill.redis.keysPrefix.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RedisService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public Object get(KeyPrefix prefix, String key) {
        String realKey = prefix.getPrefix() + key;
        return redisTemplate.opsForValue().get(realKey);
    }

//    public Object getAll(KeyPrefix prefix, String key) {
//        String realKey = prefix.getPrefix() + key;
//        return redisTemplate.opsForList().
//    }


    public <T> void set(KeyPrefix prefix, String key, Object value) {
        String realKey = prefix.getPrefix() + key;
        int seconds = prefix.expireSeconds();// 获取过期时间
        ValueOperations vOps = redisTemplate.opsForValue();
        if (seconds <= 0) {
            vOps.set(realKey, value);
        } else {
            vOps.set(realKey, value, seconds);
        }
    }

    public boolean delete(KeyPrefix prefix, String key) {
        String realKey = prefix.getPrefix() + key;
        return stringRedisTemplate.delete(realKey);
    }

    public <T> boolean exists(KeyPrefix prefix, String key) {
        String realKey = prefix.getPrefix() + key;
        return stringRedisTemplate.hasKey(realKey);
    }

    public <T> Long incr(KeyPrefix prefix, String key) {

        String realKey = prefix.getPrefix() + key;
//		return jedis.incr(realKey);
        return 1L;

    }

    public boolean decr(KeyPrefix prefix, String key) {
        //    String realKey = prefix.getPrefix() + key;
        String realKey = "a";
//        System.out.println(realKey);
        ValueOperations ops = stringRedisTemplate.opsForValue();
        int count = Integer.valueOf((String) ops.get(realKey));
//        System.out.println(count);
        AtomicInteger counter = new AtomicInteger(count);
        if (counter.get() > 0) {
            SessionCallback<List<Integer>> callback = new SessionCallback() {
                @Override
                public List<Object> execute(RedisOperations operations) throws DataAccessException {
                    operations.watch(realKey);
                    operations.multi();
                    ops.set(realKey, "" + counter.decrementAndGet());
                    return operations.exec(); //包含事务中所有操作的结果
                }
            };
            List<Integer> txResults = stringRedisTemplate.execute(callback);

            if (txResults != null && txResults.size() != 0) {
//                Long val = Long.valueOf(txResults.get(0));
                System.out.println(txResults.get(0));
//                if (val >= 0) {
                return true;
//                }
            }
        }
        return false;
    }

}
