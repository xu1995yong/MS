package com.xu.seckill.bean;

import java.util.Date;


public class MSOrder {
    private Long id;
    private Long userId;
    private Long msGoodsId;
    private Integer goodsCount;
    private Integer status;
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMsGoodsId() {
        return msGoodsId;
    }

    public void setMsGoodsId(Long msGoodsId) {
        this.msGoodsId = msGoodsId;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
