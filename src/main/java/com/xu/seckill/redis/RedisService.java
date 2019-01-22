package com.xu.seckill.redis;

import com.xu.seckill.redis.keysPrefix.KeyPrefix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class RedisService {
    private static Logger log = LoggerFactory.getLogger(RedisService.class);
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    public void initStock() {
        //初始化库存
        //    log.debug("RedisService 已经初始化成功");

    }

    public Object get(KeyPrefix prefix, Object key) {
        String realKey = prefix.getPrefix() + key;
        return redisTemplate.opsForValue().get(realKey);
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

    public boolean decrStock(KeyPrefix prefix, Object key, int goodsCount) {
        String realKey = prefix.getPrefix() + key;
        return stringRedisTemplate.opsForValue().decrement(realKey, goodsCount) >= 0;


//        long stock = Long.valueOf(stringRedisTemplate.opsForValue().get(realKey));
//        if (stock > 0) {
//            SessionCallback<List<Long>> callback = new SessionCallback<List<Long>>() {
//                @Override
//                public List<Long> execute(RedisOperations operations) throws DataAccessException {
//                    operations.watch(realKey);
//                    operations.multi();
//                    operations.opsForValue().decrement(realKey);
//                    return operations.exec(); //包含事务中所有操作的结果
//                }
//            };
//            List<Long> txResults = stringRedisTemplate.execute(callback);
//            //    log.debug("txResults {}", txResults);
//
//
//            if (txResults.size() != 0) {
//                stock = txResults.get(0);
//                if (stock < 0) {
//                    return 0;
//                }
//                return stock;
//            } else {
//                return -1;
//            }
//        } else {
//            return 0;
//        }
    }

}
