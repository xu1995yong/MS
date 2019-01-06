package com.example.demo;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;

public class SecKillRunnable implements Runnable {
    private static final String myWatchKey = "myWatchKey";
    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;

    public SecKillRunnable(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public void run() {
      //  System.out.println("线程执行");
        redisTemplate.watch(myWatchKey);
        Integer count = Integer.parseInt(valueOperations.get(myWatchKey));
      //  System.out.println("当前剩余数量：" + count);
        if (count > 0) {
      //      System.out.println("进入");
            redisTemplate.multi();
            valueOperations.increment(myWatchKey, -1);
            List<Object> list = redisTemplate.exec();
            if (list != null) {
                System.out.println("秒杀成功");
            } else {
                System.out.println("秒杀失败");
            }
        } else {
            System.out.println("秒杀结束了");
        }
    }
}
