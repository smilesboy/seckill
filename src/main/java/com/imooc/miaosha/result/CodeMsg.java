package com.imooc.miaosha.result;

public class CodeMsg {
	
	private int code;
	private String msg;
	
	//通用异常
	public static CodeMsg success = new CodeMsg(0, "success");
	public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
	public static CodeMsg Bind_ERROR = new CodeMsg(500101, "校验参数异常：%s");
	//登录模块5002XX
	public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500200, "手机号不存在");
	public static CodeMsg PASSWORD_ERROR = new CodeMsg(500201,"密码错误"); 
	//商品模块5003XX
	//订单模块5004XX
	//秒杀模块 5005XX
	
	
	
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
