package com.example.demo;

import com.xu.seckill.MsApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MsApplication.class)
public class MsApplicationTests {
    private static final String myWatchKey = "myWatchKey";
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testWatch() throws InterruptedException {
        System.out.println(redisTemplate.getDefaultSerializer());
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(myWatchKey, "10");
        ExecutorService executor = Executors.newFixedThreadPool(200);
        for (int i = 0; i < 15; i++) {
            executor.execute(new SecKillRunnable(redisTemplate));
        }
        executor.awaitTermination(30, TimeUnit.SECONDS);
    }


}
