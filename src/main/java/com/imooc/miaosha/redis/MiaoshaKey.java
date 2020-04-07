package com.imooc.miaosha.redis;

public class MiaoshaKey extends BasePrefix{

	public MiaoshaKey(String prefix) {
		super(prefix);
	}
	
	public MiaoshaKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static MiaoshaKey isGoodOver = new MiaoshaKey("go");
	public static KeyPrefix getMiaoshaPath = new MiaoshaKey(60,"mp");
	public static KeyPrefix getMiaoshaVerifyCode = new MiaoshaKey(300,"vc");
}

