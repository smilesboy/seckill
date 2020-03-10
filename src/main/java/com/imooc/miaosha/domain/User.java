package com.imooc.miaosha.domain;

import java.util.Date;

public class User {
	private Long id;
	private String nickname;
	private String password;
	private String salt;
	private String head;
	private Date register_date;
	private Date last_login_date;
	private Integer login_count;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public Date getRegister_date() {
		return register_date;
	}
	public void setRegister_date(Date register_date) {
		this.register_date = register_date;
	}
	public Date getLast_login_date() {
		return last_login_date;
	}
	public void setLast_login_date(Date last_login_date) {
		this.last_login_date = last_login_date;
	}
	public Integer getLogin_count() {
		return login_count;
	}
	public void setLogin_count(Integer login_count) {
		this.login_count = login_count;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", nickname=" + nickname + "]";
	}
	
	
}
