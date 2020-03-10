package com.imooc.miaosha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.miaosha.dao.UserDao;
import com.imooc.miaosha.domain.User;

@Service
public class UserService {
	
	@Autowired
	UserDao userDao;
	
	public List<User> list(){
		return userDao.list();
	}
}
