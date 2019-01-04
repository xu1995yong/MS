package com.xu.seckill.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class RedisService {

	@Autowired
	RedisTemplate<String, String> redisTemplate;

	public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {

		// 对key增加前缀，即可用于分类，也避免key重复
		String realKey = prefix.getPrefix() + key;
		String str = redisTemplate.opsForValue().get(realKey);
		T t = stringToBean(str, clazz);
		return t;

	}

	public <T> Boolean set(KeyPrefix prefix, String key, T value) {

		String str = beanToString(value);
		if (str == null || str.length() <= 0) {
			return false;
		}
		String realKey = prefix.getPrefix() + key;
		int seconds = prefix.expireSeconds();// 获取过期时间
		if (seconds <= 0) {
			redisTemplate.opsForValue().set(realKey, str);
		} else {
			redisTemplate.opsForValue().set(realKey, str, seconds);
		}

		return true;
	}

	public boolean delete(KeyPrefix prefix, String key) {
		// 生成真正的key
		String realKey = prefix.getPrefix() + key;
		return redisTemplate.delete(realKey);
	}

	public <T> boolean exists(KeyPrefix prefix, String key) {
		// 生成真正的key
		String realKey = prefix.getPrefix() + key;
		return redisTemplate.hasKey(realKey);
	}

	public <T> Long incr(KeyPrefix prefix, String key) {

		String realKey = prefix.getPrefix() + key;
//		return jedis.incr(realKey);
		return 1L;

	}

	public <T> Long decr(KeyPrefix prefix, String key) {

		return 1L;

	}

	public static <T> String beanToString(T value) {
		if (value == null) {
			return null;
		}
		Class<?> clazz = value.getClass();
		if (clazz == int.class || clazz == Integer.class) {
			return String.valueOf(value);
		} else if (clazz == long.class || clazz == Long.class) {
			return String.valueOf(value);
		} else if (clazz == String.class) {
			return (String) value;
		} else {
			return JSON.toJSONString(value);
		}

	}

	public static <T> T stringToBean(String str, Class<T> clazz) {
		if (str == null || str.length() <= 0 || clazz == null) {
			return null;
		}
		if (clazz == int.class || clazz == Integer.class) {
			return (T) Integer.valueOf(str);
		} else if (clazz == long.class || clazz == Long.class) {
			return (T) Long.valueOf(str);
		} else if (clazz == String.class) {
			return (T) str;
		} else {
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}
	}

}
