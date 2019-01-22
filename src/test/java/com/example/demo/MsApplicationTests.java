package com.example.demo;

import com.xu.seckill.MsApplication;
import com.xu.seckill.bean.Goods;
import com.xu.seckill.bean.Order;
import com.xu.seckill.mq.MQSender;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.redis.keysPrefix.GoodsKey;
import com.xu.seckill.redis.keysPrefix.UserKey;
import com.xu.seckill.service.GoodsService;
import com.xu.seckill.service.MSService;
import com.xu.seckill.util.UUIDUtil;
import com.xu.seckill.vo.Person;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
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

import javax.annotation.Resource;
import java.sql.Timestamp;
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
        int fail = 0;
        int empthStock = 0;
        int success = 0;
        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {

            tasks.add(new Callable<String>() {
                          @Override
                          public String call() throws Exception {
                              return msService.seckill(1, 2);
                          }
                      }
            );
        }
        List<Future<String>> futures = pool.invokeAll(tasks);

        for (Future<String> f : futures) {
            String str = f.get();
            if (Objects.isNull(str)) {
                empthStock++;
            } else if (str.equals("")) {
                fail++;
            } else {
                success++;
            }

        }
        System.out.println("一共秒杀：" + (fail + empthStock + success));
        System.out.println("秒杀失败" + fail);
        System.out.println("库存不足" + empthStock);
        System.out.println("秒杀成功" + success);
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


    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Autowired
    MQSender mqSender;


}