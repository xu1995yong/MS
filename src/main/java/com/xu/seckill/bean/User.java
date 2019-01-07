package com.xu.seckill.bean;

import java.io.Serializable;

public class User implements Serializable {
    private Long id;
    private String phone;
    private String password;

    public User() {

    }

    public User(Long id, String phoneNumber, String password) {
        this.id = id;
        this.phone = phoneNumber;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", phoneNumber=" + phone + ", password=" + password + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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


}
