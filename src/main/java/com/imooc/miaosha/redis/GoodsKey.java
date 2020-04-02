package com.imooc.miaosha.redis;

public class GoodsKey extends BasePrefix{

	
	public GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static GoodsKey getGoodsList = new GoodsKey(60,"gl");
	public static KeyPrefix getMiaoshaGoodsStock = new GoodsKey(0,"gs");
}
