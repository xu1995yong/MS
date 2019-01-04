package com.jesper.seckill.vo;

import java.sql.Timestamp;

import com.jesper.seckill.bean.Goods;

public class GoodsVo extends Goods {
	@Override
	public String toString() {
		return "GoodsVo [seckillPrice=" + seckillPrice + ", stockCount=" + stockCount + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", version=" + version + "]";
	}

	private Double seckillPrice;
	private Integer stockCount;
	private Timestamp startDate;
	private Timestamp endDate;
	private Integer version;

	public Double getSeckillPrice() {
		return seckillPrice;
	}

	public void setSeckillPrice(Double seckillPrice) {
		this.seckillPrice = seckillPrice;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
