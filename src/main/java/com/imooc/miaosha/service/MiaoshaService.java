package com.imooc.miaosha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.redis.MiaoshaKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.vo.GoodsVo;

@Service
public class MiaoshaService {
	
	@Autowired
	GoodsService goodService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	RedisService redisService;
	/*
	 * 1.减库存
	 * 2.写入订单信息
	 */
	@Transactional
	public OrderInfo miaosha(MiaoshaUser user,GoodsVo goods) {
		boolean success = goodService.reduceStock(goods);
		if(success) {
			return orderService.createOrder(user,goods);
		} else {
			setGoodsOver(goods.getId());
			return null;
		}
		
	}
	
	public long getMiaoshaResult(Long userId, long goodsId) {
		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
		if(order != null) {//秒杀成功
			return order.getOrderId();
		}else {
			boolean isOver = getGoodsOver(goodsId);
			if(isOver) {
				return -1;
			}else {
				return 0;
			}
		}
	}
	
	private void setGoodsOver(Long goodsId) {
		redisService.set(MiaoshaKey.isGoodOver, ""+goodsId, true);
		
	}

	private boolean getGoodsOver(long goodsId) {
		return redisService.exists(MiaoshaKey.isGoodOver, ""+goodsId);
	}
	
}
