package com.xu.seckill.vo;

import com.xu.seckill.validator.IsMobile;

import javax.validation.constraints.NotNull;


public class LoginVo {

    //因为框架没有校验手机格式注解，所以自己定义
    private String phone;

    private String password;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginVo{" +
                "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
