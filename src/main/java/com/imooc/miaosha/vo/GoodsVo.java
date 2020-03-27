package com.imooc.miaosha.vo;

import java.util.Date;

import com.imooc.miaosha.domain.Goods;

public class GoodsVo extends Goods{
	private Double MiaoshaPrice;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
	public Double getMiaoshaPrice() {
		return MiaoshaPrice;
	}
	public void setMiaoshaPrice(Double miaoshaPrice) {
		MiaoshaPrice = miaoshaPrice;
	}
	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}
