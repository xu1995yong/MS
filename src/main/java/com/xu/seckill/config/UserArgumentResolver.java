package com.xu.seckill.config;

import com.alibaba.druid.util.StringUtils;
import com.xu.seckill.bean.User;
import com.xu.seckill.controller.SeckillController;
import com.xu.seckill.service.UserService;
import com.xu.seckill.util.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    private static Logger log = LoggerFactory.getLogger(UserArgumentResolver.class);
    @Autowired
    UserService userService;

    //当参数类型为User才做处理
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //获取参数类型
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == User.class;
    }

    /**
     * 思路：先获取到已有参数HttpServletRequest，从中获取到token，再用token作为key从redis拿到User，而HttpServletResponse作用是为了延迟有效期
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        Cookie[] cookies = request.getCookies();

        String cookieToken = CookieUtils.getCookieByName(cookies, UserService.COOKIE_NAME_TOKEN).getValue();
        return userService.getByToken(response, cookieToken);

    }
}
