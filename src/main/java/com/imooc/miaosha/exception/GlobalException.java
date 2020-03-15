package com.imooc.miaosha.exception;

import com.imooc.miaosha.result.CodeMsg;

//编写自定义异常类
public class GlobalException extends RuntimeException{

	private static final Long serialVersionUID = 1L;
	
	private CodeMsg cm;
	
	public GlobalException(CodeMsg cm) {
		super(cm.toString());
		this.cm = cm;
	}
	
	public CodeMsg getCm() {
		return cm;
	}
}
