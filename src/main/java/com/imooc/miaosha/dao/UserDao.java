package com.imooc.miaosha.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.imooc.miaosha.domain.User;

@Mapper
public interface UserDao {
	
	@Select("select * from miaosha_user")
	public List<User> list();
}
