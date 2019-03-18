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

    public User getByPhone(String phone) {
        return userMapper.getByPhone(phone);
    }


    public String login(HttpServletRequest request, HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        boolean success = logout(request, response);
//        if (!success) {
//            return null;
//        }
        String mobile = loginVo.getPhone();
        String formPass = loginVo.getPassword();
        // 判断手机号是否存在
        User user = getByPhone(mobile);
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
        redisService.set(UserKey.USER_TOKEN, token, user);
        addCookie(response, token);
        return token;
    }

    /**
     * 将token做为key，用户信息做为value 存入redis模拟session 同时将token存入cookie，保存登录状态
     */
    public void addCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.USER_TOKEN.EXPIRE_TIME);
        cookie.setPath("/");// 设置为网站根目录
        response.addCookie(cookie);
    }


    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = CookieUtils.getCookieByName(cookies, COOKIE_NAME_TOKEN);
        if (cookie != null) {
            boolean success = redisService.delete(UserKey.USER_TOKEN, "" + cookie.getValue());
            Cookie newcookie = CookieUtils.delCookie(cookies, cookie);
            response.addCookie(newcookie);
            return success;
        }
        return true;
    }


    public User getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = (User) redisService.get(UserKey.USER_TOKEN, token);
        // 延长有效期，有效期等于最后一次操作+有效期
        if (user != null) {
            addCookie(response, token);
        }
        return user;
    }

}
