package com.imooc.miaosha.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.miaosha.domain.User;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.UserKey;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.UserService;

@Controller
@RequestMapping("/demo")
public class DemoController {
	
	private static Logger log = LoggerFactory.getLogger(DemoController.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	RedisService redisService;
	
	/*
	 * 连接mysql数据库
	 * 从mysql数据库获取用户信息
	 */
	@RequestMapping("/list")
	@ResponseBody
	public String list(){
		List<User> list1 = userService.list();
		for(User user: list1) {
			log.info(user.toString());
		}
		return "success";
	}
	
	/*
	 * 连接redis数据库
	 * 写入redis数据
	 */
	@RequestMapping("/redis")
	@ResponseBody
	public String redis(){
		redisService.set(UserKey.getById, "us", "111");
		return "success";
	}
	
	/*
	 * 返回结果状态码
	 */
	
	@RequestMapping("/cm")
	@ResponseBody
	public Result<Boolean> cm(){
		return Result.success(true);
	}
	
}
