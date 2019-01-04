package com.jesper.seckill.bean;

import java.sql.Timestamp;

public class SeckillGoods {
	private Long id;
	private Long goodsId;
	private double seckillPrice;
	private Integer stockCount;
	private Timestamp startDate;
	private Timestamp endDate;
	private int version;

	@Override
	public String toString() {
		return "SeckillGoods [id=" + id + ", goodsId=" + goodsId + ", stockCount=" + stockCount + ", startDate="
				+ startDate + ", endDate=" + endDate + ", version=" + version + "]";
	}

	public SeckillGoods() {
		super();
	}

	public SeckillGoods(Long id, Long goodsId, Integer stockCount, Timestamp startDate, Timestamp endDate,
			int version) {
		super();
		this.id = id;
		this.goodsId = goodsId;
		this.stockCount = stockCount;
		this.startDate = startDate;
		this.endDate = endDate;
		this.version = version;
	}

	public double getSeckillPrice() {
		return seckillPrice;
	}

	public void setSeckillPrice(double seckillPrice) {
		this.seckillPrice = seckillPrice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getStockCount() {
		return stockCount;
	}

	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
