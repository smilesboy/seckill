package com.imooc.miaosha.result;

public class CodeMsg {
	
	
	private int code;
	private String msg;
	
	//通用异常
	public static CodeMsg success = new CodeMsg(0, "success");
	public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
	public static CodeMsg Bind_ERROR = new CodeMsg(500101, "校验参数异常：%s");
	public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "请求非法");
	public static CodeMsg VERIFYCODE_REEOR = new CodeMsg(500103, "验证码错误");
	public static CodeMsg ACCESS_LIMIT_REACHE = new CodeMsg(500104, "访问太频繁");
	
	//登录模块5002XX
	public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500200, "手机号不存在");
	public static CodeMsg PASSWORD_ERROR = new CodeMsg(500201,"密码错误"); 
	public static CodeMsg SESSION_ERROR = new CodeMsg(500102, "Session不存在或者已经失效");
	//商品模块5003XX
	//订单模块5004XX
	//秒杀模块 5005XX
	public static CodeMsg MIAO_SHA_OVER = new CodeMsg(500500,"秒杀已经结束"); 
	public static CodeMsg REPEAT_MIAOSHA = new CodeMsg(500501,"不能重复秒杀");
	public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500502, "订单不存在");
	public static CodeMsg MIAOSHA_FAIL = new CodeMsg(500503, "秒杀失败");
	
	
	
	
	 
	
	public CodeMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
	
	public CodeMsg fillArgs(Object... args) {
		int code = this.code;
		String message = String.format(this.msg, args);
		return new CodeMsg(code, message);
	}

	@Override
	public String toString() {
		return "CodeMsg [code=" + code + ", msg=" + msg + "]";
	}
	
	
}
