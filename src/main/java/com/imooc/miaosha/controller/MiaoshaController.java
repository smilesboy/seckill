package com.imooc.miaosha.controller;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.miaosha.access.AccessLimit;
import com.imooc.miaosha.domain.Goods;
import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.rabbitmq.MiaoshaMessage;
import com.imooc.miaosha.redis.AccessKey;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean{
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	MiaoshaService miaoshaService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	MQSender mqSender;
	
	private Map<Long,Boolean> localOverMap = new HashMap<Long, Boolean>();
	
	/*
	 * 系统初始化
	 */
	
	@Override
	public void afterPropertiesSet() throws Exception {
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		if(goodsList == null) {
			return;
		}
		for(GoodsVo goods : goodsList) {
			redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());
			localOverMap.put(goods.getId(), false);
		}
	}

	@RequestMapping(value="/{path}/do_miaosha",method=RequestMethod.POST)
	@ResponseBody
	public Result<Integer> do_miaosha(Model model,MiaoshaUser user
			,@RequestParam("goodsId")Long goodsId
			,@PathVariable("path")String path) {
		//判断用户是否登录
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		//验证path
		boolean check = miaoshaService.checkPath(user,goodsId,path);
		if(!check) {
			Result.error(CodeMsg.REQUEST_ILLEGAL);
		}
		
		//内存标记，减少redis访问
		boolean over = localOverMap.get(goodsId);
		if(over) {
			return Result.error(CodeMsg.MIAO_SHA_OVER);
		}
		
		//判断是否已经秒杀到
		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
		if(order != null) {
			return Result.error(CodeMsg.REPEAT_MIAOSHA);
		}
		
		//预减库存
		long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
		if(stock < 0) {
			return Result.error(CodeMsg.MIAO_SHA_OVER);
		}
		
		//入队
		MiaoshaMessage mm = new MiaoshaMessage();
		mm.setUser(user);
		mm.setGoodsId(goodsId);
		mqSender.sendMiaoshaMessage(mm);
		
		return Result.success(0);
		
		
	}
	@AccessLimit(seconds=5, maxCount=5, needLogin=true)
	@RequestMapping(value="/path",method=RequestMethod.GET)
	@ResponseBody
	public Result<String> getMiaoshaPath(HttpServletRequest request,MiaoshaUser user,@RequestParam("goodsId")Long goodsId,
			@RequestParam(value = "verifyCode",defaultValue="0")int verifyCode) {
		//判断用户是否登录
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		
		
		//验证验证码
		boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
		if(!check) {
			return Result.error(CodeMsg.VERIFYCODE_REEOR);
		}
		//生成秒杀path
		String path = miaoshaService.createMiaoshaPath(user,goodsId);
		return Result.success(path);
	}
	
	@RequestMapping(value="/verifyCode",method=RequestMethod.GET)
	@ResponseBody
	public Result<String> getMiaoshaVerifyCode(HttpServletResponse response,MiaoshaUser user,
			@RequestParam("goodsId")Long goodsId) {
		//判断用户是否登录
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		//生成verifyCode
		try {
			BufferedImage image = miaoshaService.createVerifyCode(user,goodsId);
			OutputStream out = response.getOutputStream();
    		ImageIO.write(image, "JPEG", out);
    		out.flush();
    		out.close();
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return Result.error(CodeMsg.MIAOSHA_FAIL);
		}
		
	}
	
	/*
	 * orderId:成功
	 * 1：秒杀失败
	 * 0：排队中
	 */
	@RequestMapping(value="/result",method=RequestMethod.GET)
	@ResponseBody
	public Result<Long> miaoshaResult(Model model,MiaoshaUser user,
			@RequestParam("goodsId")long goodsId) {
		model.addAttribute("user",user);
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		long result = miaoshaService.getMiaoshaResult(user.getId(),goodsId);
		return Result.success(result);
	}

}
