package com.imooc.miaosha.redis;

public class AccessKey extends BasePrefix{

	public AccessKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
	//public static AccessKey access = new AccessKey(5, "ak");
	public static AccessKey withExpire(int expireSeconds) {
		return new AccessKey(expireSeconds, "access");
	}

}
