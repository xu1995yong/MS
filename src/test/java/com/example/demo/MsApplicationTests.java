package com.example.demo;

import com.xu.seckill.MsApplication;
import com.xu.seckill.bean.Goods;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.redis.keysPrefix.GoodsKey;
import com.xu.seckill.redis.keysPrefix.UserKey;
import com.xu.seckill.service.GoodsService;
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
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MsApplication.class)
public class MsApplicationTests {
    private static final String myWatchKey = "myWatchKey";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testWatch() throws InterruptedException, ExecutionException {

        ValueOperations<String, String> strOps = stringRedisTemplate.opsForValue();
//        stringRedisTemplate.delete(myWatchKey);
        strOps.set(myWatchKey, "10");
//        Thread.sleep(10);
        ExecutorService pool = Executors.newSingleThreadExecutor();
        List<Callable<Object>> tasks = new ArrayList<>();
        for (int i = 0; i < 15; i++) {

            tasks.add(new Callable() {
                @Override
                public Object call() throws Exception {
                    return stringRedisTemplate.execute(new SessionCallback() {
                        @Override
                        public Object execute(RedisOperations operations) throws DataAccessException {
                            operations.watch(myWatchKey);
                            //   if (count > 0) {
//                            try {
//                                //      Thread.sleep(new Random().nextInt() * 10);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                            System.out.println(operations.opsForValue().get(myWatchKey));
                            operations.multi();
//                            int count = Integer.valueOf((String) operations.opsForValue().get(myWatchKey));

                            Long val = operations.opsForValue().increment(myWatchKey, -1);
                            System.out.println(val);
                            return operations.exec();

//                            } else {
//                                System.out.println("秒杀结束了");
//                            }
                        }
                    });
                }
            });
        }
        List<Future<Object>> futures = pool.invokeAll(tasks);
        for (Future<Object> f : futures) {
            List list = (ArrayList) f.get();
            if (list != null && list.size() != 0) {
                System.out.println(list);
                System.out.println("秒杀成功");
            } else {
                System.out.println("秒杀失败");
            }
        }
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
            tasks.add(new Callable() {
                @Override
                public Boolean call() throws Exception {
                    return redisService.decr(GoodsKey.GOODS_STOCK, String.valueOf(1));
                }
            });
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