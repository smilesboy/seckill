package com.imooc.miaosha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.vo.GoodsVo;

@Service
public class MiaoshaService {
	
	@Autowired
	GoodsService goodService;
	
	@Autowired
	OrderService orderService;
	/*
	 * 1.减库存
	 * 2.写入订单信息
	 */
	public OrderInfo miaosha(MiaoshaUser user,GoodsVo goods) {
		goodService.reduceStock(goods);
		return orderService.createOrder(user,goods);
	}
	
}
