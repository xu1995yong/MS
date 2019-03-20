package com.example.demo;

import com.google.common.util.concurrent.RateLimiter;
import com.xu.seckill.MsApplication;
import com.xu.seckill.bean.Goods;
import com.xu.seckill.controller.MSController;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.redis.keysPrefix.GoodsKey;
import com.xu.seckill.service.GoodsService;
import com.xu.seckill.service.MSService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MsApplication.class)
public class MsApplicationTests {

    @Autowired
    private MSService seckillService;

    @Test
    public void testSeckill() throws InterruptedException, ExecutionException {
        int limited = 0;
        int fail = 0;
        int success = 0;
        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Callable<String> task = new Callable() {
                @Override
                public String call() throws Exception {
                    String orderId = seckillService.seckill(1, 1, 1);
                    return orderId;
                }
            };
            tasks.add(task);
        }
        List<Future<String>> futures = pool.invokeAll(tasks);

        for (Future<String> f : futures) {
            try {
                String str = f.get();


            } catch (Exception e) {
                // System.out.println(e);
            }
        }

        pool.awaitTermination(1000, TimeUnit.MILLISECONDS);

        Thread.sleep(10000);
    }

    @Autowired
    RedisService redisService;

    @Test
    public void testRedisService() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        List<Callable<Object>> tasks = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
//            tasks.add(new Callable() {
//                @Override
//                public int call() throws Exception {
//                    return redisService.decr(GoodsKey.GOODS_STOCK, String.valueOf(1));
//                }
//            });
        }
        List<Future<Object>> futures = pool.invokeAll(tasks);
        for (Future f : futures) {
            System.out.println(f.get());
        }
    }

    @Autowired
    GoodsService goodsService;

    @Test
    public void testRedis() {
        Goods goods = goodsService.getGoodsById(1);
        System.out.println(goods);
        redisService.set(GoodsKey.GOODS_DETAIL, "g", goods);
        System.out.println(redisService.get(GoodsKey.GOODS_DETAIL, "g"));
//        redisService.set(UserKey.ID, "id", p);
//        System.out.println(redisService.get(UserKey.ID, "id"));
    }


}