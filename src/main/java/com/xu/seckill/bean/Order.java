package com.xu.seckill.bean;

import java.io.Serializable;
import java.sql.Timestamp;


public class Order implements Serializable {
    private String id;
    private Long userId;
    private Long goodsId;
    private Integer goodsCount;
    private Integer status;
    private Timestamp createDate;

    public Order() {
    }

    public Order(String id, Long userId, Long goodsId, Integer goodsCount, Integer status, Timestamp createDate) {
        this.id = id;
        this.userId = userId;
        this.goodsId = goodsId;
        this.goodsCount = goodsCount;
        this.status = status;
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", goodsId=" + goodsId +
                ", goodsCount=" + goodsCount +
                ", status=" + status +
                ", createDate=" + createDate +
                '}';
    }
}
