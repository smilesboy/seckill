package com.imooc.miaosha.redis;

public class MiaoshaKey extends BasePrefix{
	private MiaoshaKey(String prefix) {
		super(prefix);
	}
	
	public static MiaoshaKey isGoodOver = new MiaoshaKey("go");
}
