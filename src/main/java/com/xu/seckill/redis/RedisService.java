package com.xu.seckill.redis;

import com.xu.seckill.redis.keysPrefix.KeyPrefix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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

    //    public <T> Long incr(KeyPrefix prefix, String key) {
//
//        String realKey = prefix.getPrefix() + key;
////		return jedis.incr(realKey);
//        return 1L;
//
//    }

    //redis预减库存
    public long decrStock(KeyPrefix prefix, Object key, int goodsCount) {
        //redis中是否存在该key
        String realKey = prefix.getPrefix() + key;

        SessionCallback<List<Long>> callback = new SessionCallback<List<Long>>() {
            @Override
            public List<Long> execute(RedisOperations operations) throws DataAccessException {
                operations.watch(realKey);
                operations.multi();
                operations.opsForValue().decrement(realKey, goodsCount);
                return operations.exec(); //包含事务中所有操作的结果
            }
        };

        List<Long> txResults = stringRedisTemplate.execute(callback);
        if ((txResults.size() == 0)) {
            return -1;
        }

        return txResults.get(0);
    }
}


