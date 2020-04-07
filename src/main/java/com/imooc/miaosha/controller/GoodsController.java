package com.imooc.miaosha.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.vo.GoodsDetailVo;
import com.imooc.miaosha.vo.GoodsVo;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	
	public static Logger log = LoggerFactory.getLogger(GoodsController.class);
	
	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	ApplicationContext applicationContext;
	
	//商品列表页（页面缓存）
	@RequestMapping(value="/to_list",produces="text/html")
	@ResponseBody
	public String list(HttpServletRequest request,HttpServletResponse response,Model model,MiaoshaUser user) {
		model.addAttribute("user",user);
		//取缓存
		String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
		if(!StringUtils.isEmpty(html)) {
			return html;
		}
		//查询商品列表
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		model.addAttribute("goodsList", goodsList);
		
		SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(),
				request.getLocale(), model.asMap(), applicationContext);
		//手动渲染
		html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
		if(!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsList, "", html);
		}
		return html;
		//return "goods_list";
	}
	
	//商品详情页
	
	@RequestMapping(value="/detail/{goodsId}",produces="application/json")
	@ResponseBody
	public Result<GoodsDetailVo> goodsDetail(HttpServletRequest request,HttpServletResponse response,Model model,
			MiaoshaUser user,@PathVariable("goodsId")Long goodsId) {
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		
		
		//秒杀数据计算
		long startTime = goods.getStartDate().getTime();
		long endTime = goods.getEndDate().getTime();
		long now = System.currentTimeMillis();
		
		int miaoshaStatus = 0;
		int remainSeconds = 0;
			
		if(now < startTime) {  //秒杀还没开始
			miaoshaStatus = 0;
			remainSeconds = (int) ((startTime - now)/1000);
		} else if(now > endTime) { //秒杀已经结束
			miaoshaStatus = 2;
			remainSeconds = -1;
		} else {  //秒杀进行中
			miaoshaStatus = 1;
			remainSeconds = 0;
		}
		
		//model.addAttribute("miaoshaStatus",miaoshaStatus);
		//model.addAttribute("remainSeconds",remainSeconds);
		
		GoodsDetailVo vo = new GoodsDetailVo();
		vo.setGoods(goods);
		vo.setUser(user);
		vo.setRemainSeconds(remainSeconds);
		vo.setMiaoshaStatus(miaoshaStatus);
		return Result.success(vo);
	    //return "goods_detail";
	}
}
