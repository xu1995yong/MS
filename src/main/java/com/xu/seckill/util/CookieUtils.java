package com.xu.seckill.util;

import com.xu.seckill.config.UserArgumentResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtils {
    private static final Logger log = LoggerFactory.getLogger(CookieUtils.class);

    //遍历所有cookie，找到需要的那个cookie
    public static Cookie getCookieByName(Cookie[] cookies, String cookiName) {
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookiName)) {
                return cookie;
            }
        }
        return null;
    }

    public static Cookie delCookie(Cookie[] cookies, Cookie cookie) {
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie tCookie : cookies) {
            if (cookie == tCookie) {
                cookie.setValue(null);
                cookie.setMaxAge(0);// 立即销毁cookie
                cookie.setPath("/");
                return cookie;
            }
        }
        return null;
    }
}
