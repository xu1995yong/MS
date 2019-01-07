package com.xu.seckill.service;

import com.alibaba.druid.util.StringUtils;
import com.xu.seckill.bean.User;
import com.xu.seckill.exception.GlobalException;
import com.xu.seckill.mapper.UserMapper;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.redis.keysPrefix.UserKey;
import com.xu.seckill.result.CodeMsg;
import com.xu.seckill.util.CookieUtils;
import com.xu.seckill.util.UUIDUtil;
import com.xu.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisService redisService;

    public static final String COOKIE_NAME_TOKEN = "token";

    public User getById(long id) {
        return userMapper.getById(id);
    }

    /**
     * 典型缓存同步场景：更新密码
     */
//    public boolean updatePassword(String token, long id, String formPass) {
//        // 取user
//        User user = getById(id);
//        if (user == null) {
//            throw new GlobalException(CodeMsg.USER_ERROR);
//        }
//        // 更新数据库
//        User toBeUpdate = new User();
//        toBeUpdate.setId(id);
//        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
//        userMapper.update(toBeUpdate);
//        // 更新缓存：先删除再插入
//        redisService.delete(UserKey.token, "" + id);
//        user.setPassword(toBeUpdate.getPassword());
//        redisService.set(UserKey.token, token, user);
//        return true;
//    }
    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        // 判断手机号是否存在
        User user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.USER_ERROR);
        }
        // 验证密码
        String dbPass = user.getPassword();
        if (!formPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.USER_ERROR);
        }
        // 生成唯一id作为token
        String token = UUIDUtil.uuid();
        redisService.set(UserKey.TOKEN, token, user);
        addCookie(response, token);
        return token;
    }

    /**
     * 将token做为key，用户信息做为value 存入redis模拟session 同时将token存入cookie，保存登录状态
     */
    public void addCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.TOKEN.expireSeconds());
        cookie.setPath("/");// 设置为网站根目录
        response.addCookie(cookie);
    }


    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = CookieUtils.getCookieByName(cookies, COOKIE_NAME_TOKEN);
        boolean success = redisService.delete(UserKey.TOKEN, "" + cookie.getValue());
        Cookie newcookie = CookieUtils.delCookie(cookies, cookie);
        response.addCookie(newcookie);
        return success;
    }


    public User getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = (User) redisService.get(UserKey.TOKEN, token);
        // 延长有效期，有效期等于最后一次操作+有效期
        if (user != null) {
            addCookie(response, token);
        }
        return user;
    }

}
