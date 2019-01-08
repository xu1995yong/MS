package com.example.demo;

import com.xu.seckill.MsApplication;
import com.xu.seckill.bean.Goods;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.redis.keysPrefix.GoodsKey;
import com.xu.seckill.redis.keysPrefix.UserKey;
import com.xu.seckill.service.GoodsService;
import com.xu.seckill.service.MSService;
import com.xu.seckill.vo.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MsApplication.class)
public class MsApplicationTests {
    private static final String myWatchKey = "myWatchKey";
    @Autowired
    private MSService msService;

    @Test
    public void testSeckill() throws InterruptedException, ExecutionException {
        int count_0 = 0;
        int count_1 = 0;
        ExecutorService pool = Executors.newFixedThreadPool(50);
        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {

            tasks.add(new Callable<String>() {
                          @Override
                          public String call() throws Exception {
                              return msService.seckill(1, 1);
                          }
                      }
            );
        }
        List<Future<String>> futures = pool.invokeAll(tasks);

//        for (Future<String> f : futures) {
//            String str = f.get();
//
//        }
//        System.out.println("一共秒杀：" + (count__1 + count_0 + count_1));
//        System.out.println("秒杀失败" + count__1);
        System.out.println("库存不足" + count_0);
        System.out.println("秒杀成功" + count_1);
        pool.shutdown();
        pool.awaitTermination(1000, TimeUnit.MILLISECONDS);
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